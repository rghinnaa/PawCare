package com.example.pawcare.data.remote.source.data

import com.example.pawcare.data.remote.api.ApiCallback
import com.example.pawcare.utils.flowResponse

class HomeRemoteDataSource(callback: ApiCallback) {
    private val apiCallback = callback

    fun requestBanner(token: String) = flowResponse {
        apiCallback.requestBanner(token)
    }

    fun requestDoctor(token: String, keyword: String, items: String) = flowResponse {
        apiCallback.requestDoctor(token, keyword, items)
    }
}