package com.iaguapacha.blogapp.presentation.auth

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.iaguapacha.blogapp.core.Result
import com.iaguapacha.blogapp.domain.auth.AuthRepo
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class AuthViewModel(private val repo:AuthRepo):ViewModel() {


    fun signIn(email:String,passsword:String) = liveData(Dispatchers.IO){
        emit(Result.Loading())

        try {
            emit(Result.Succes(repo.signIn(email,passsword)))
        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }

    fun signUp(email:String,password:String, username:String) = liveData(Dispatchers.IO){
        emit(Result.Loading())
        try {
            emit(Result.Succes(repo.signUp(email,password,username)))
        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }

    fun updateUserProfile(imageBitmap:Bitmap,username:String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Succes(repo.updateProfile(imageBitmap,username)))
        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }

}

class AuthViewModelFactory(private val repo:AuthRepo):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(repo) as T
    }
}