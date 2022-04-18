package com.univerindream.maicaiassistant

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.Utils
import com.elvishew.xlog.XLog

object MHDefault {

    val githubSolutions = arrayListOf<MCSolution>()

    val defaultMCSolutions: List<MCSolution> by lazy {
        val res = arrayListOf<MCSolution>()

        try {
            val solutions = Utils.getApp().assets.list("solutions")
            solutions?.forEach {
                val json = ResourceUtils.readAssets2String("solutions/$it")
                res.add(GsonUtils.fromJson(json, MCSolution::class.java))
            }
        } catch (e: Exception) {
            XLog.e(e)
        }

        res.add(
            MCSolution(
                name = "自定义",
                steps = arrayListOf()
            ),
        )

        res
    }

}