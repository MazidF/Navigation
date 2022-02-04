package com.example.netflix

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.netflix.databinding.FragmentProfileBinding


class FragmentProfile(val launcher: ActivityResultLauncher<Intent>) : Fragment(R.layout.fragment_profile) {
    private lateinit var dialog: AlertDialog.Builder
    lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        init()
    }

    private fun init() {
        dialog = AlertDialog.Builder(requireContext()).apply {
            setItems(arrayOf("Camera", "Gallery"),
                DialogInterface.OnClickListener { _, index ->
                    val intent = if (index == 0) {
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    } else {
                        Intent(Intent.ACTION_PICK).apply {
                            type = "image/*"
                        }
                    }
                    launcher.launch(intent)
                })
        }
        with(binding) {
            profileImageEdit.setOnClickListener {
                dialog.show()
            }
        }
    }
}
