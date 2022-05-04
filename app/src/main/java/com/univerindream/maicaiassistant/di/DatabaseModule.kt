package com.univerindream.maicaiassistant.di

import com.univerindream.maicaiassistant.data.GithubRepository
import com.univerindream.maicaiassistant.data.DataRepository
import com.univerindream.maicaiassistant.api.GithubService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideMCRepository(): DataRepository {
        return DataRepository()
    }

    @Singleton
    @Provides
    fun provideGithubRepository(githubService: GithubService): GithubRepository {
        return GithubRepository(githubService)
    }

}