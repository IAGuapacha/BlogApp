package com.iaguapacha.blogapp.domain.auth

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseUser
import com.iaguapacha.blogapp.data.remote.auth.AuthDataSource

class AuthRepoImpl(private val dataSource: AuthDataSource) : AuthRepo {


    override suspend fun signIn(email: String, password: String): FirebaseUser? =
        dataSource.signIn(email, password)

    override suspend fun signUp(email: String, password: String, username: String): FirebaseUser? =
        dataSource.signUp(email, password, username)

    override suspend fun updateProfile(imageBitmap: Bitmap, username: String) =
        dataSource.updateUserProfile(imageBitmap, username)

}