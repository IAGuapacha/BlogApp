package com.iaguapacha.blogapp.domain.home

import com.iaguapacha.blogapp.core.Result
import com.iaguapacha.blogapp.data.model.Post

interface HomeScreenRepo {

    suspend fun getLastestPosts(): Result<List<Post>>
}