package com.iaguapacha.blogapp.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.iaguapacha.blogapp.R
import com.iaguapacha.blogapp.core.Result
import com.iaguapacha.blogapp.data.remote.auth.AuthDataSource
import com.iaguapacha.blogapp.databinding.FragmentRegisterBinding
import com.iaguapacha.blogapp.domain.auth.AuthRepoImpl
import com.iaguapacha.blogapp.presentation.auth.AuthViewModel
import com.iaguapacha.blogapp.presentation.auth.AuthViewModelFactory


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding

    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        signUp()

    }

    private fun signUp() {


        binding.btnSignUp.setOnClickListener {

            val username = binding.editTextUsername.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()

            Log.d("signUpData", "data $username $email $password $confirmPassword ")
            if (validateUserData(
                    password,
                    confirmPassword,
                    email,
                    username
                )
            ) return@setOnClickListener

            createUser(email, password, username)
        }
    }

    private fun createUser(email: String, password: String, username: String) {
        viewModel.signUp(email, password, username).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSignUp.isEnabled = false

                }
                is Result.Succes -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_registerFragment_to_homeScreenFragment)
                }

                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignUp.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        "Error: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        })

    }

    private fun validateUserData(
        password: String,
        confirmPassword: String,
        email: String,
        username: String
    ): Boolean {
        if (password != confirmPassword) {
            binding.editTextConfirmPassword.error = "Password does not match"
            binding.editTextPassword.error = "Password does not match"
            return true
        }

        if (password.isEmpty()) {
            binding.editTextPassword.error = "Password is empty"
            return true
        }

        if (confirmPassword.isEmpty()) {
            binding.editTextConfirmPassword.error = "Password is empty"
            return true
        }

        if (email.isEmpty()) {
            binding.editTextEmail.error = "Password is empty"
            return true
        }

        if (username.isEmpty()) {
            binding.editTextUsername.error = "Password is empty"
            return true
        }
        return false
    }


}