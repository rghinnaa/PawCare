package com.example.pawcare.data.remote.source.callback

import com.example.pawcare.data.remote.Result
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow

interface MainSourceCallback {

    fun requestLogin(body: JsonElement): Flow<Result<Any>>

    fun requestRegister(body: JsonElement): Flow<Result<Any>>

    fun requestForgotPassword(body: JsonElement): Flow<Result<Any>>

}