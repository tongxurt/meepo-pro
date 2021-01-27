package com.meepo.settings.nightmode

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import com.meepo.settings.Store
import com.meepo.settings.UserSettingsModule.requireAppContext

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/11/2 10:40 PM
 * @version 1.0
 */
object NightModeHelper {

    fun switchMode(delegate: AppCompatDelegate, mode: String) {
        delegate.localNightMode = mapTo(mode)
    }


    fun initNighMode(mode: String) {
        AppCompatDelegate.setDefaultNightMode(mapTo(mode))
    }

    private fun mapTo(mode: String): Int {
        if (mode == Store.NIGHT_MODE_NIGHT) {
            return AppCompatDelegate.MODE_NIGHT_YES
        } else if (mode == Store.NIGHT_MODE_NORMAL) {
            return AppCompatDelegate.MODE_NIGHT_NO
        } else if (mode == Store.NIGHT_MODE_FOLLOW_SYSTEM) {
            return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            return AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        }
    }
}