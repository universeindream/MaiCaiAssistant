package com.univerindream.maicaiassistant.viewmodels

import androidx.lifecycle.ViewModel
import com.univerindream.maicaiassistant.data.DataRepository
import com.univerindream.maicaiassistant.data.GithubRepository
import com.univerindream.maicaiassistant.model.MCSolution
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocalSolutionViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val githubRepository: GithubRepository
) : ViewModel() {

    fun getPublicSolutions() = githubRepository.getPublicSolutions()
}