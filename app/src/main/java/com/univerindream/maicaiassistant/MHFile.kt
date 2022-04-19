package com.univerindream.maicaiassistant

import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.PathUtils
import java.util.*

object MHFile {

    private const val SOLUTIONS_PATH = "solutions"

    private val rootSolutionsPath: String by lazy {
        PathUtils.join(PathUtils.getExternalAppFilesPath(), SOLUTIONS_PATH)
    }

    fun save(uuid: String = UUID.randomUUID().toString(), json: String) {
        val fileName = "$uuid.json"
        FileIOUtils.writeFileFromString(PathUtils.join(rootSolutionsPath, fileName), json)
    }

    fun list() {

    }

}