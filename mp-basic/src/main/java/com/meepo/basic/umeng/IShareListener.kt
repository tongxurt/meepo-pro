package com.meepo.basic.umeng

interface IShareListener {
    fun onStart(media: String)

    fun onResult(media: String)

    fun onError(media: String, e: Throwable)

    fun onCancel(media: String)
}