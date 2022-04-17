package com.univerindream.maicaiassistant.api

import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Url


interface GithubApi {

    @Headers("User-Agent: MaiCaiAssistant")
    @GET("/repos/{owner}/{repo}/releases")
    suspend fun searchRepos(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
    ): List<GithubReleases>

    @Headers("User-Agent: MaiCaiAssistant")
    @GET("/repos/{owner}/{repo}/contents/{path}")
    suspend fun getContents(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String,
    ): List<GithubContents>

    @Headers("User-Agent: MaiCaiAssistant", "Cache-Control: private")
    @GET
    suspend fun downloadFileWithDynamicUrlSync(@Url fileUrl: String): ResponseBody

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