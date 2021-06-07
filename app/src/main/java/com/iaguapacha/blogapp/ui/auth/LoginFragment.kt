package com.iaguapacha.blogapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.iaguapacha.blogapp.R
import com.iaguapacha.blogapp.core.Result
import com.iaguapacha.blogapp.data.remote.auth.AuthDataSource
import com.iaguapacha.blogapp.databinding.FragmentLoginBinding
import com.iaguapacha.blogapp.domain.auth.AuthRepoImpl
import com.iaguapacha.blogapp.presentation.auth.AuthViewModel
import com.iaguapacha.blogapp.presentation.auth.AuthViewModelFactory


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        isUserLoggedIn()
        doLogin()
        goToSignUp()

    }

    private fun goToSignUp(){
        binding.txtSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun isUserLoggedIn() {
        firebaseAuth.currentUser?.let {user ->
            if(user.displayName.isNullOrEmpty()){
                findNavController().navigate(R.id.action_loginFragment_to_setupProfileFragment)
            }else{
                findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
            }
        }
    }

    private fun doLogin() {
        binding.btnSignin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()
            valitatedCredentials(email, password)
            signIn(email, password)
        }
    }

    private fun valitatedCredentials(email: String, password: String) {
        if (email.isEmpty()) {
            binding.editTextEmail.error = "E-mail is empty"
            return
        }
        if (email.isEmpty()) {
            binding.editPassword.error = "Password is empty"
            return
        }
    }

    private fun signIn(email: String, password: String) {
        viewModel.signIn(email, password).observe(viewLifecycleOwner, { result ->
            when (result) {

                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSignin.isEnabled = false
                }

                is Result.Succes -> {
                    binding.progressBar.visibility = View.GONE

                    if(result.data?.displayName.isNullOrEmpty()){
                        findNavController().navigate(R.id.action_loginFragment_to_setupProfileFragment)
                    }else{
                        findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
                    }
                }

                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignin.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        "Error ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }


}