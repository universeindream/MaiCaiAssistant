package com.univerindream.maicaiassistant

import com.google.gson.Gson

object MHConfig {

    const val MAI_CAI_ASSISTANT_CHANNEL_ID = "MaiCaiAssistant_fixed"

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