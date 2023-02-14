package com.example.pawcare.data.remote.source.data

import com.example.pawcare.data.remote.api.ApiCallback
import com.example.pawcare.utils.flowResponse
import com.google.gson.JsonElement

class MainRemoteDataSource(callback: ApiCallback) {
    private val apiCallback = callback

    fun requestLogin(body: JsonElement) = flowResponse {
        apiCallback.requestLogin(body)
    }

    fun requestRegister(body: JsonElement) = flowResponse {
        apiCallback.requestRegister(body)
    }

    fun requestForgotPassword(body: JsonElement) = flowResponse {
        apiCallback.requestForgotPassword(body)
    }
}