package com.univerindream.maicaiassistant

import com.google.gson.Gson

object MHConfig {

    const val MALL_HELP_CHANNEL_ID = "mallhelp_1"

    var curMCSolution: MCSolution = MCSolution("", arrayListOf())
        get() {
            return if (field.name.isBlank()) {
                Gson().fromJson(MHData.curMCSolutionJSON, MCSolution::class.java)
            } else {
                field
            }
        }
        set(value) {
            field = value
            MHData.curMCSolutionJSON = Gson().toJson(field)
        }
}