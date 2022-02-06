package com.example.netflix

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color.RED
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.netflix.databinding.FragmentProfileBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import java.util.Calendar.*


class FragmentProfile(private val launcher: ActivityResultLauncher<Intent>) :
    Fragment(R.layout.fragment_profile) {
    private lateinit var dialog: AlertDialog.Builder
    lateinit var binding: FragmentProfileBinding
    private val model: ViewModelNetflix by activityViewModels()
    private val saver: ViewModelProfile by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        init()
    }

    private fun init() {
        dialogInit()
        with(binding) {
            model.image.observe(viewLifecycleOwner) {
                if (it != null) {
                    profileImage.setImageBitmap(it)
                }
            }
            profileImageEdit.setOnClickListener {
                dialog.show()
            }
            profileBirthdayLayout.setEndIconOnClickListener {
                val datePicker = pikerMaker(profileBirthday)
                datePicker.show()
            }
            val simpleRegex = Regex("^(\\s?[a-zA-Z]*)+$")
            profileName.setRegex(simpleRegex)
            profileFamily.setRegex(simpleRegex)
            profileEmail.setRegex(Regex("^\\w+([\\.-]?\\w+)*$"))
            profilePhone.setRegex(Regex("^(0|\\+98)9(1[0-9]|3[1-9])[0-9]{7}$"))
            profileBirthday.setRegex(Regex("^\\d{4}/\\d{2}/\\d{2}$"))
            profileRegister.setOnClickListener {
                if (checker()) {
                    model.user = binding.userMaker()
                    model.hasRegistered.value = true
                }
            }
            profileNameLayout.setHelperTextColor(ColorStateList.valueOf(RED))
            profileFamilyLayout.setHelperTextColor(ColorStateList.valueOf(RED))
            profileEmailLayout.setHelperTextColor(ColorStateList.valueOf(RED))
        }
        load()
    }

    private fun FragmentProfileBinding.userMaker(): NetflixUser {
        return NetflixUser(
            profileName.text!!.trim().toString(),
            profileFamily.text!!.trim().toString(),
            profileEmail.text!!.trim().toString() + "@gmail.com"
        ).apply {
            profileUsername.text?.let {
                if (it.isNotBlank()) {
                    this.userName = it.toString()
                }
            }
            this.phone = profilePhone.text.toString()
            this.birthday = profileBirthday.text.toString()
            this.image = model.image.value
        }
    }

    private fun checker(): Boolean {

        fun checkIfNotEmpty(editText: TextInputEditText, parent: TextInputLayout): Boolean {
            var result = false
            parent.helperText = if (editText.text!!.isBlank()) {
                "*Required"
            } else if (editText.currentTextColor == RED) {
                "Wrong Input"
            } else {
                result = true
                ""
            }
            if (!result) {
                editText.requestFocus()
            }
            return result
        }

        fun checkIfValid(editText: TextInputEditText): Boolean {
            if (editText.text!!.isNotBlank() && editText.currentTextColor == RED) {
                editText.requestFocus()
                return false
            }
            return true
        }

        var result: Boolean
        with(binding) {
            result = checkIfValid(profileBirthday)
            result = result and checkIfValid(profilePhone)
            result = result and checkIfNotEmpty(profileEmail, profileEmailLayout)
            result = result and checkIfNotEmpty(profileFamily, profileFamilyLayout)
            result = result and checkIfNotEmpty(profileName, profileNameLayout)
        }
        return result
    }

    private fun pikerMaker(editText: TextInputEditText): DatePickerDialog {
        val cal = Calendar.getInstance()
        return DatePickerDialog(requireContext(), { _, year, month, day ->
            editText.setText(String.format("%4d/%02d/%02d", year, month + 1, day))
        }, cal.get(YEAR), cal[MONTH], cal[DAY_OF_MONTH])
    }

    private fun TextInputEditText.setRegex(regex: Regex) {
        val color = currentTextColor
        doOnTextChanged { text, _, _, _ ->
            if (!regex.matches(text!!)) {
                this.setTextColor(RED)
            } else {
                this.setTextColor(color)
            }
        }
    }

    private fun dialogInit() {
        dialog = AlertDialog.Builder(requireContext()).apply {
            setItems(arrayOf("Camera", "Gallery"),
                DialogInterface.OnClickListener { _, index ->
                    val intent = if (index == 0) {
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    } else {
                        val i = Intent(Intent.ACTION_PICK).apply {
                            type = "image/*"
                        }
                        Intent.createChooser(i, "Choose a picture")
                    }
                    launcher.launch(intent)
                })
        }
    }

    private fun log(msg: String) {
        Log.d("tag-tag", msg)
    }

    override fun onPause() {
        save()
        super.onPause()
    }

    private fun save() {
        with(binding) {
            with(saver) {
                username = profileUsername.text.toString()
                name = profileName.text.toString()
                family = profileFamily.text.toString()
                email = profileEmail.text.toString()
                phone = profilePhone.text.toString()
                birthday = profileBirthday.text.toString()
                hasBeenSet = true
            }
        }
    }

    private fun load() {
        with(binding) {
            with(saver) {
                if (hasBeenSet) {
                    profileUsername.setText(username)
                    profileName.setText(name)
                    profileFamily.setText(family)
                    profileEmail.setText(email)
                    profilePhone.setText(phone)
                    profileBirthday.setText(birthday)

                    hasBeenSet = false
                } else if (model.hasRegistered.value!!) {
                    val user = model.user!!
                    profileUsername.setText(user.userName)
                    profileName.setText(user.name)
                    profileFamily.setText(user.family)
                    profileEmail.setText(user.email.replace("@gmail.com", ""))
                    profilePhone.setText(user.phone)
                    profileBirthday.setText(user.birthday)
                    profileImage.setImageBitmap(user.image)
                    user.image?.let {
                        model.image.value = it
                    }
                    hasBeenSet = false
                }
            }
        }
    }
}
