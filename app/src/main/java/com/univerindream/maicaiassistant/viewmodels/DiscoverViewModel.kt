package com.univerindream.maicaiassistant.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.univerindream.maicaiassistant.data.DataRepository
import com.univerindream.maicaiassistant.data.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val githubRepository: GithubRepository
) : ViewModel() {

    val showProgress: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    fun getPublicSolutions() = githubRepository.getPublicSolutions()
}