package com.example.netflix

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color.RED
import android.os.Bundle
import android.util.Log
import android.view.Gravity.CENTER
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.netflix.databinding.FragmentHomeBinding

class FragmentHome : Fragment(R.layout.fragment_home) {
    var hasRegistered = false
    val homeModel: ViewModelSaveLayout by viewModels()
    val model: ViewModelNetflix by activityViewModels()
    lateinit var binding: FragmentHomeBinding
    lateinit var dialog: AlertDialog.Builder
    private val context by lazy {
        requireActivity()
    }
    val controller by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log("onViewCreated")
        binding = FragmentHomeBinding.bind(view)
        init()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init() {
        with(binding.homeList) {
            val list = homeModel.linearLayouts.value!!
            if (list.isNotEmpty()) {
                hasRegistered = model.hasRegistered.value!!
                for (linear in list) {
                    linear.parent
                    addView(linear)
                }
            } else {
                val param = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1F)
                val bitmap = context.resources.getDrawable(R.drawable.icon_movie).toBitmap()

                for (i in 0 .. 6) {
                    addView(addLinear(i, bitmap, param))
                }
            }
        }
        with(model) {
            hasRegistered.observe(viewLifecycleOwner) {
                this@FragmentHome.hasRegistered = it
            }
        }
        dialog = AlertDialog.Builder(context)
            .setTitle("You should register first!!")
            .setPositiveButton("Register") { _, _ ->
                controller.navigate(FragmentHomeDirections.actionFragmentHomeToFragmentProfile())
            }.setNeutralButton("Cancel") { _, _ ->

            }
    }

    private fun addLinear(int: Int, bitmap: Bitmap, param: LinearLayout.LayoutParams): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = CENTER
            var number = int * 3 + 1
            addView(view("Movie${number}", bitmap, number++), param)
            addView(view("Movie${number}", bitmap, number++), param)
            addView(view("Movie$number", bitmap, number), param)
        }.also {
            homeModel.addLinear(it)
        }
    }

    @SuppressLint("SetTextI18n")
    fun view(name: String, image: Bitmap, index: Int): View {
        val movie = Movie(name, index).apply {
            this.image = image
        }
        return movie.getView(context) { imageView, view ->
            imageView.apply {
                tag = false
                setOnClickListener {
                    if (!hasRegistered) {
                        dialog.show()
                        return@setOnClickListener
                    }
                    tag = if (tag == true) {
                        unlike(view.tag as View)
                        clearColorFilter()
                        false
                    } else {
                        like(movie, view, imageView)
                        setColorFilter(RED)
                        true
                    }
                }
            }
        }.apply {
            model.movies.value?.add(this)
        }
    }

    private fun like(movie: Movie, liked: View, likedImage: AppCompatImageView) {
        val view = movie.getView(requireContext()) { imageView, view ->
            imageView.apply {
                setColorFilter(RED)
                setOnClickListener {
                    unlike(view)
                    likedImage.apply {
                        clearColorFilter()
                        tag = false
                    }
                }
            }
        }
        liked.tag = view
        model.addFavorite(view)
    }

    private fun unlike(view: View) {
        model.removeFavorite(view)
    }

    override fun onStop() {
        super.onStop()
        binding.homeList.removeAllViews()
        log("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    fun log(msg: String) {
        Log.d("tag-tag", msg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        log("attach")
    }

    override fun onDetach() {
        super.onDetach()
        log("detach")
    }

    override fun onStart() {
        super.onStart()
        log("onStart")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        log("onSave")
    }
}
