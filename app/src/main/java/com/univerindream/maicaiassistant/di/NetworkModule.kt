package com.univerindream.maicaiassistant.di

import com.univerindream.maicaiassistant.api.GithubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideGithubService(): GithubService {
        return GithubService.create()
    }
}