package com.iaguapacha.blogapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.iaguapacha.blogapp.R
import com.iaguapacha.blogapp.core.Result
import com.iaguapacha.blogapp.data.remote.home.HomeScreenDataSource
import com.iaguapacha.blogapp.databinding.FragmentHomeScreenBinding
import com.iaguapacha.blogapp.domain.home.HomeScreenRepoImpl
import com.iaguapacha.blogapp.presentation.main.HomeScreenViewModel
import com.iaguapacha.blogapp.presentation.main.HomeScreenViewModelFactory
import com.iaguapacha.blogapp.ui.main.adapter.HomeScreenAdapter


class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel by viewModels<HomeScreenViewModel> {
        HomeScreenViewModelFactory(HomeScreenRepoImpl(HomeScreenDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)

        viewModel.fetchLatestPosts().observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Succes -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvHome.adapter = HomeScreenAdapter(result.data)

                }
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Ocurrio un error: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}