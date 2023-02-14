package com.example.pawcare.data.remote.repository

import com.example.pawcare.data.remote.source.callback.MainSourceCallback
import com.example.pawcare.data.remote.source.data.MainRemoteDataSource
import com.google.gson.JsonElement

class MainRepository(
    mainRemoteDataSource: MainRemoteDataSource
) : MainSourceCallback {
    private val remoteDataSource = mainRemoteDataSource

    override fun requestLogin(body: JsonElement) = remoteDataSource.requestLogin(body)

    override fun requestRegister(body: JsonElement) = remoteDataSource.requestRegister(body)

    override fun requestForgotPassword(body: JsonElement) =
        remoteDataSource.requestForgotPassword(body)
}