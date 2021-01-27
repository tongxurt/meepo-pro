package com.meepo.settings

import android.util.Log
import com.meepo.design.selector.OptionGroup
import com.meepo.design.settings.SettingsGroupItem
import com.meepo.design.settings.SettingsItem
import com.meepo.sdk.manager.CacheManager
import com.meepo.sdk.store.persistence.sp.SharedPrefsFactory
import com.meepo.settings.UserSettingsModule.requireAppContext
import com.meepo.settings.Store.State.FONT_SIZE_OPTIONS
import com.meepo.settings.nightmode.NightModeHelper
import com.meepo.settings.schema.UserSettings

object Store {

    const val STORE_KEY_SETTINGS = "user_settings"
    const val SETTING_ITEM_NIGHT_MODE = "nightMode"
    const val SETTING_ITEM_FONT_SIZE = "fontSize"
    const val SETTING_ITEM_CLEAR_CACHE = "clearCache"
    const val SETTING_ITEM_CHECK_UPDATE = "checkUpdate"

    const val NIGHT_MODE_FOLLOW_SYSTEM = "followSystem"
    const val NIGHT_MODE_NORMAL = "normal"
    const val NIGHT_MODE_NIGHT = "night"
    const val NIGHT_MODE_AUTO = "auto"

    object Schema {
//        data class UserSettings(
//            var nightMode: String,
//            var fontSize: Int,
//            var cacheSize: String
//        )

//        data class AppUpgrade(
//            var update: Boolean = false,
//            var newVersion: String = "",
//            var upgradeLog: String = "",
//            var title: String = "",
//            var targetSize: String = "",
//            var constraint: Boolean = false,
//            var apkFileMd5: String = "",
//            var ignorable: Boolean = false
//        )

    }

    internal object State {
        internal val userSettings = UserSettings(
            nightMode = NIGHT_MODE_NORMAL,
            fontSize = 1,
            cacheSize = "0M"
        )

        internal val SETTINGS_GROUPS: ArrayList<SettingsGroupItem> = arrayListOf(
            SettingsGroupItem(
                "", "",
                arrayListOf(
                    SettingsItem(SETTING_ITEM_NIGHT_MODE, "深色模式"),
                    SettingsItem(SETTING_ITEM_FONT_SIZE, "字体大小"),
                    SettingsItem(SETTING_ITEM_CLEAR_CACHE, "清理缓存"),
                    SettingsItem(
                        SETTING_ITEM_CHECK_UPDATE,
                        "检查更新",
                        rightText = BuildConfig.VERSION_NAME
                    )
                )
            )
        )

        internal val NIGHT_MODE_OPTION_GROUPS: ArrayList<OptionGroup> = arrayListOf(
            OptionGroup(
                "",
                arrayListOf(
                    OptionGroup.Option(NIGHT_MODE_FOLLOW_SYSTEM, "跟随系统"),
                    OptionGroup.Option(NIGHT_MODE_NORMAL, "普通模式", true),
                    OptionGroup.Option(NIGHT_MODE_NIGHT, "深色模式"),
                    OptionGroup.Option(NIGHT_MODE_AUTO, "自动")
                )
            )
        )

        internal val FONT_SIZE_OPTIONS: Array<String> = arrayOf(
            "小", "中", "大", "特大"
        )
    }

    internal object Func {

        fun saveNightMode(nightMode: String) {
            State.userSettings.nightMode = nightMode
            saveUserSettings()
        }


        fun saveFontSize(fontSize: Int) {
            State.userSettings.fontSize = fontSize
            saveUserSettings()
        }

        private fun saveUserSettings() {
            SharedPrefsFactory.setUp(requireAppContext())
                .setObject(STORE_KEY_SETTINGS, State.userSettings)

        }

        fun fetchAndApplyUserSettings() {
            SharedPrefsFactory.setUp(requireAppContext())
                .getObject(STORE_KEY_SETTINGS, State.userSettings::class.java)?.let {
                    applyNightMode(it.nightMode)
                    applyFontSize(it.fontSize)
                    applyCacheSize()
                }
        }

        private fun applyCacheSize() {
            val cacheSize = CacheManager.getTotalCacheSize(requireAppContext())
            State.SETTINGS_GROUPS.forEach { g ->
                g.groupItems.forEach { s ->
                    if (s.key.equals(SETTING_ITEM_CLEAR_CACHE)) {
                        s.rightText = cacheSize
                    }
                }
            }
        }


        private fun applyFontSize(itemSelected: Int) {
            State.userSettings.fontSize = itemSelected
            State.SETTINGS_GROUPS.forEach { g ->
                g.groupItems.forEach { s ->
                    if (s.key.equals(SETTING_ITEM_FONT_SIZE)) {
                        s.rightText = FONT_SIZE_OPTIONS[itemSelected]
                    }
                }
            }
        }

        private fun applyNightMode(nightNode: String) {
            State.userSettings.nightMode = nightNode

            var name = ""

            State.NIGHT_MODE_OPTION_GROUPS.forEach { g ->
                g.options.forEach { o ->

                    if (o.key.equals(nightNode)) {
                        name = o.title
                        o.isChecked = true
                    } else {
                        o.isChecked = false
                    }
                }
            }

            Log.e("applyNightMode", name)


            State.SETTINGS_GROUPS.forEach { g ->
                g.groupItems.forEach { s ->
                    if (s.key.equals(SETTING_ITEM_NIGHT_MODE)) {
                        s.rightText = name
                    }
                }
            }
        }

        fun initNighMode() {
            SharedPrefsFactory.setUp(requireAppContext())
                .getObject(STORE_KEY_SETTINGS, UserSettings::class.java)?.let {
                    NightModeHelper.initNighMode(it.nightMode)
                }
        }
    }
}