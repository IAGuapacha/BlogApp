package com.iaguapacha.blogapp.data.remote.home

import com.google.firebase.firestore.FirebaseFirestore
import com.iaguapacha.blogapp.core.Result
import com.iaguapacha.blogapp.data.model.Post
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {

    suspend fun getLatestPosts(): Result<List<Post>> {
        val postList = mutableListOf<Post>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("post").get().await()

        for (post in querySnapshot.documents){
            post.toObject(Post::class.java)?.let { fbPost ->
                postList.add(fbPost)
            }
        }

        return  Result.Succes(postList)
    }
}