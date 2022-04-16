package com.univerindream.maicaiassistant.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {

    @GET("/repos/{owner}/{repo}/releases")
    suspend fun searchRepos(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
    ): List<GithubReleases>


    companion object {
        private const val BASE_URL = "https://api.github.com/"

        private val retrofit by lazy {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
        }

        private val githubApi by lazy {
            retrofit.create(GithubApi::class.java)
        }

        fun get(): GithubApi {
            return githubApi
        }
    }
}