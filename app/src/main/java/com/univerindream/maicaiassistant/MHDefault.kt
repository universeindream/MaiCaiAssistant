package com.univerindream.maicaiassistant

import com.blankj.utilcode.util.ResourceUtils
import com.elvishew.xlog.XLog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MHDefault {

    val s3Solutions = arrayListOf<MCSolution>()

    val defaultMCSolutions: List<MCSolution> by lazy {
        val res = arrayListOf<MCSolution>()

        try {
            val json = ResourceUtils.readAssets2String("solutions.json")
            res.addAll(Gson().fromJson<List<MCSolution>>(json, object : TypeToken<ArrayList<MCSolution>>() {}.type))
        } catch (e: Exception) {
            XLog.e(e)
        }

        res
    }

}