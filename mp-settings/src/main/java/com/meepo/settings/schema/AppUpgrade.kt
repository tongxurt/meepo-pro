package com.meepo.settings.schema

data class AppUpgrade(
    var update: Boolean = false,
    var newVersion: String = "",
    var upgradeLog: String = "",
    var title: String = "",
    var targetSize: String = "",
    var constraint: Boolean = false,
    var apkFileMd5: String = "",
    var ignorable: Boolean = false
)
