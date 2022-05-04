package com.univerindream.maicaiassistant.api

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Url


interface GithubService {

    @Headers("User-Agent: MaiCaiAssistant")
    @GET("/repos/{owner}/{repo}/releases")
    suspend fun searchReleases(
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

        fun create(): GithubService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubService::class.java)
        }
    }
}