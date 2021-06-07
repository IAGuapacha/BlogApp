package com.iaguapacha.blogapp.ui.auth

import android.app.Activity
import android.app.AlertDialog
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.iaguapacha.blogapp.R
import com.iaguapacha.blogapp.core.Result
import com.iaguapacha.blogapp.data.remote.auth.AuthDataSource
import com.iaguapacha.blogapp.databinding.FragmentSetupProfileBinding
import com.iaguapacha.blogapp.domain.auth.AuthRepoImpl
import com.iaguapacha.blogapp.presentation.auth.AuthViewModel
import com.iaguapacha.blogapp.presentation.auth.AuthViewModelFactory


class SetupProfileFragment : Fragment(R.layout.fragment_setup_profile) {

    private lateinit var binding: FragmentSetupProfileBinding
    private var bitmap: Bitmap? = null

    private var resultLauncher =
        this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val imageBitmap = data?.extras?.get("data") as Bitmap
                binding.profileImage.setImageBitmap(imageBitmap)
                bitmap = imageBitmap

            }
        }

    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupProfileBinding.bind(view)

        binding.profileImage.setOnClickListener {

            try {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultLauncher.launch(takePictureIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(),
                    "No se encontro una app para abrir la camara",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnCreateProfile.setOnClickListener {
            val username = binding.etxtUsername.text.toString().trim()
            val alertDialog =
                AlertDialog.Builder(requireContext()).setTitle("Uploadin photo...").create()
            bitmap?.let {
                if (username.isNotEmpty()) {
                    viewModel.updateUserProfile(it, username)
                        .observe(viewLifecycleOwner, { result ->
                            when (result) {
                                is Result.Loading -> {

                                }

                                is Result.Succes -> {
                                    alertDialog.dismiss()
                                    findNavController().navigate(R.id.action_setupProfileFragment_to_homeScreenFragment)
                                }

                                is Result.Failure -> {
                                    alertDialog.dismiss()
                                }
                            }
                        })
                }
            }

        }
    }
}