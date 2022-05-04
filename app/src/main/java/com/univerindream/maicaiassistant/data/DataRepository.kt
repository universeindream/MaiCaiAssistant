package com.univerindream.maicaiassistant.data

import com.univerindream.maicaiassistant.MCData
import com.univerindream.maicaiassistant.MCFile
import com.univerindream.maicaiassistant.model.MCSolution
import javax.inject.Singleton

@Singleton
class DataRepository {

    val localSolutions: ArrayList<MCSolution>
        get() {
            val data = MCFile.listSolution()
            data.sortByDescending { it.updateDateStr + it.name }
            return data
        }

    var curSolution: MCSolution = MCFile.getSolution(MCData.curSolutionId)
        get() = MCFile.getSolution(MCData.curSolutionId)
        set(value) {
            field = value
            MCData.curSolutionId = value.id
        }

    var timerStatus: Boolean = MCData.timerStatus
        get() = MCData.timerStatus
        set(value) {
            field = value
            MCData.timerStatus = value
        }

    var timerTime: Long = MCData.timerTime
        get() = MCData.timerTime
        set(value) {
            field = value
            MCData.timerTime = value
        }

    fun isExistSolution(solutionId: String) = MCFile.isExistSolution(solutionId)

    fun saveSolution(solution: MCSolution): Boolean = MCFile.saveSolution(solution)

    fun deleteSolution(solutionId: String) = MCFile.deleteSolution(solutionId)
}