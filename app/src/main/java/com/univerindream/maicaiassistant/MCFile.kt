package com.univerindream.maicaiassistant

import com.blankj.utilcode.util.*
import com.univerindream.maicaiassistant.model.MCSolution

object MCFile {

    private const val SOLUTIONS_PATH = "solutions"

    private val rootSolutionsPath: String by lazy {
        PathUtils.join(PathUtils.getExternalAppFilesPath(), SOLUTIONS_PATH)
    }

    fun isExistSolution(solutionId: String) = FileUtils.isFileExists(PathUtils.join(rootSolutionsPath, "$solutionId.json"))

    fun firstSolution(): MCSolution {
        var solution = listSolution().firstOrNull()
        if (solution == null) {
            solution = MCSolution(name = "自定义")
            saveSolution(solution)

            try {
                val solutions = Utils.getApp().assets.list("solutions")
                solutions?.forEach {
                    val json = ResourceUtils.readAssets2String("solutions/$it")
                    saveSolution(GsonUtils.fromJson(json, MCSolution::class.java))
                }
            } catch (e: Exception) {
                LogUtils.e(e.message)
            }
        }
        return solution
    }

    fun getSolution(solutionId: String): MCSolution {
        val json = FileIOUtils.readFile2String(PathUtils.join(rootSolutionsPath, "$solutionId.json"))
        return GsonUtils.fromJson(json, MCSolution::class.java)
    }

    fun saveSolution(solution: MCSolution): Boolean {
        val fileName = "${solution.id}.json"
        return FileIOUtils.writeFileFromString(PathUtils.join(rootSolutionsPath, fileName), GsonUtils.toJson(solution))
    }

    fun deleteSolution(solutionId: String): Boolean {
        val fileName = "$solutionId.json"
        return FileUtils.delete(PathUtils.join(rootSolutionsPath, fileName))
    }

    fun listSolution(): ArrayList<MCSolution> {
        val result = arrayListOf<MCSolution>()
        val list = FileUtils.listFilesInDir(rootSolutionsPath)
        list.forEach {
            try {
                val json = FileIOUtils.readFile2String(it)
                result.add(GsonUtils.fromJson(json, MCSolution::class.java))
            } catch (e: Exception) {
                LogUtils.e(e)
            }
        }
        return result
    }

}