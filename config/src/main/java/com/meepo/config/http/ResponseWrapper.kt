package com.meepo.config.http

import androidx.annotation.Nullable
import java.io.Serializable

class ResponseWrapper<T>(
    val code: Int,
    @Nullable val data: T,
    @Nullable val message: String?
) : Serializable