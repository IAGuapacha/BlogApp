package com.iaguapacha.blogapp.domain.home

import com.iaguapacha.blogapp.core.Result
import com.iaguapacha.blogapp.data.model.Post
import com.iaguapacha.blogapp.data.remote.home.HomeScreenDataSource

class HomeScreenRepoImpl(private val dataSource: HomeScreenDataSource): HomeScreenRepo {


    override suspend fun getLastestPosts(): Result<List<Post>> = dataSource.getLatestPosts()
}