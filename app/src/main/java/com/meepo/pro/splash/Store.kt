package com.meepo.pro.splash

import java.io.Serializable

object Store {

    const val REQ_PATH_APP_SETTINGS = "/api/v1/app/settings"


    object Schema {
        data class AppSettings(
            var id: String
        )
    }

    object State {

    }

    object Func {

    }
}