package com.univerindream.maicaiassistant.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.univerindream.maicaiassistant.data.DataRepository
import com.univerindream.maicaiassistant.model.MCSolution
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    val curSolution: MutableLiveData<MCSolution> by lazy {
        MutableLiveData(dataRepository.curSolution)
    }

    val localSolutions: MutableLiveData<ArrayList<MCSolution>> by lazy {
        MutableLiveData(dataRepository.localSolutions)
    }

    fun setCurSolution(solution: MCSolution) {
        dataRepository.curSolution = solution
        curSolution.value = solution
    }

    fun saveSolution(solution: MCSolution) {
        dataRepository.saveSolution(solution)
        localSolutions.value = dataRepository.localSolutions

        if (solution.id == dataRepository.curSolution.id) {
            curSolution.value = solution
            return
        }
    }

    fun isExistSolution(solutionId: String): Boolean = dataRepository.isExistSolution(solutionId)

    fun getSolution(solutionId: String): MCSolution {
        return if (solutionId.isBlank()) {
            dataRepository.curSolution
        } else {
            dataRepository.localSolutions.first { it.id == solutionId }
        }
    }

    fun deleteSolution(solutionId: String): Boolean {
        val res = dataRepository.deleteSolution(solutionId)
        localSolutions.value = dataRepository.localSolutions
        return res
    }
}