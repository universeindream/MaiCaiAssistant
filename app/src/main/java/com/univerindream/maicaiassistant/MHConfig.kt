package com.univerindream.maicaiassistant

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MHConfig {

    const val MALL_HELP_CHANNEL_ID = "mallhelp_1"

    var curMHSolution: MHSolution = MHSolution("", arrayListOf())
        get() {
            return if (field.name.isBlank()) {
                Gson().fromJson(MHData.curJsonMHSolution, MHSolution::class.java)
            } else {
                field
            }
        }
        set(value) {
            field = value
            MHData.curJsonMHSolution = Gson().toJson(field)
        }

    var allMHSolution: List<MHSolution> = arrayListOf()
        get() = Gson().fromJson(
            MHData.allJsonMHSolution,
            object : TypeToken<ArrayList<MHSolution>>() {}.type
        )
        set(value) {
            field = value
            MHData.allJsonMHSolution = Gson().toJson(field)
        }
}