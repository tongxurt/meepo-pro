package com.meepo.basic.settings

import com.meepo.basic.schema.AppSettings
import com.meepo.sdk.observer.Observer
import com.meepo.sdk.observer.ObserverManager.applySchedulers

internal object State {

    var appSettings = AppSettings(
        id = "id"
    )

    fun update() {
        Service.get().fetchAppSettings()
            .applySchedulers()
            .map { rsp -> rsp.data }
            .subscribe(object : Observer<AppSettings>() {
                override fun onSuccess(t: AppSettings?) {
                    t?.let {
                        appSettings = it
                    }
                }

                override fun onFailure(e: Throwable) {
                }
            })
    }
}