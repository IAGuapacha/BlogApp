package com.iaguapacha.blogapp.ui.camera

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.iaguapacha.blogapp.R
import com.iaguapacha.blogapp.databinding.FragmentCameraBinding

class CameraFragment : Fragment(R.layout.fragment_camera) {


    private lateinit var binding:FragmentCameraBinding
    private var resultLauncher = this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imgAddPhoto.setImageBitmap(imageBitmap)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraBinding.bind(view)

        try {
            openSomeActivityForResult()
        }catch (e:ActivityNotFoundException){
            Toast.makeText(requireContext(),"No se encontro ninguna app para abrir la camara",Toast.LENGTH_SHORT).show()
        }
    }

    fun openSomeActivityForResult() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePictureIntent)
    }

}