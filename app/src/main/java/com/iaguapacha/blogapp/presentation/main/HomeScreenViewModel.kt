package com.iaguapacha.blogapp.presentation.main


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.iaguapacha.blogapp.core.Result
import com.iaguapacha.blogapp.domain.home.HomeScreenRepo
import kotlinx.coroutines.Dispatchers

class HomeScreenViewModel(private val repo: HomeScreenRepo):ViewModel() {

    fun fetchLatestPosts() = liveData(Dispatchers.IO){
        emit(Result.Loading())
        try {
            emit(repo.getLastestPosts())
        }catch (e:Exception){
            emit(Result.Failure(e))
        }

    }
}

class HomeScreenViewModelFactory(private val repo: HomeScreenRepo):ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HomeScreenRepo::class.java).newInstance(repo)
    }
}