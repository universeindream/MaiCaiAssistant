package com.univerindream.maicaiassistant.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.univerindream.maicaiassistant.api.GithubReleases
import com.univerindream.maicaiassistant.api.GithubService
import com.univerindream.maicaiassistant.model.MCSolution
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(private val githubService: GithubService) {

    fun latestReleases(): Flow<Result<List<GithubReleases>>> = flow {
        try {
            val data = githubService.searchReleases("universeindream", "MaiCaiAssistant")
            emit(Result.success(data))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getPublicSolutions(): Flow<Result<List<MCSolution>>> = flow {
        try {
            val json =
                githubService.downloadFileWithDynamicUrlSync("https://maicaiassistant.s3.ap-east-1.amazonaws.com/v3.json")
                    .string()
            val solutions = Gson().fromJson<ArrayList<MCSolution>>(json, object : TypeToken<List<MCSolution>>() {}.type)
            solutions.sortByDescending { it.updateDateStr + it.name }
            emit(Result.success(solutions))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

}