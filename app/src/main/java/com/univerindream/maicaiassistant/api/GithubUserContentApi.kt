package com.univerindream.maicaiassistant.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubUserContentApi {

    @GET("universeindream/MaiCaiAssistant/main/{path}")
    suspend fun getFile(
        @Path(value = "path") path: String
    ): Any

    companion object {
        private const val BASE_URL = "https://raw.githubusercontent.com/"

        private val retrofit by lazy {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
        }

        private val githubApi by lazy {
            retrofit.create(GithubUserContentApi::class.java)
        }

        fun get(): GithubUserContentApi {
            return githubApi
        }

    }
}