package com.meepo.basic.auth

import com.meepo.basic.AppBasicModule.requireAppContext
import com.meepo.basic.schema.UserSummary
import com.meepo.sdk.store.persistence.sp.SharedPrefsFactory

internal object Store {

    const val RESEND_CODE_DURATION = 60
    const val LOGIN_PASSWORD_MIN_LENGTH = 8

    const val SETTINGS_ITEM_SETTINGS = "settings"
    const val LOGIN_METHOD_CODE = "code"
    const val LOGIN_METHOD_PASSWORD = "password"
//    const val LOGIN_METHOD_ONE_KEY = "oneKey"

    const val USER_SUMMARY_KEY = "userSummaryKey"

    internal object State {
        var userSummary: UserSummary? = null
    }

    internal object Func {
        fun saveUserSummary(userSummary: UserSummary) {

            State.userSummary = userSummary
            SharedPrefsFactory.setUp(requireAppContext())
                .setObject(USER_SUMMARY_KEY, userSummary)
        }

        fun getUserSummary(): UserSummary? {

            var rsp: UserSummary? = State.userSummary

            if (rsp == null) {
                rsp = SharedPrefsFactory.setUp(requireAppContext()).getObject(USER_SUMMARY_KEY, UserSummary::class.java)
                State.userSummary = rsp
            }
            return rsp
        }

        fun clearUserSummary() {
            State.userSummary = null
            SharedPrefsFactory.setUp(requireAppContext())
                .remove(USER_SUMMARY_KEY)
        }
    }
}