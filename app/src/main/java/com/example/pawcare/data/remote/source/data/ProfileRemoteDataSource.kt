package com.example.pawcare.data.remote.source.data

import com.example.pawcare.data.remote.api.ApiCallback
import com.example.pawcare.utils.flowResponse
import com.google.gson.JsonElement
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileRemoteDataSource(callback: ApiCallback) {
    private val apiCallback = callback

    fun requestUserDetail(token: String) = flowResponse {
        apiCallback.requestUserDetail(token)
    }

    fun requestHistoryList(token: String, status: String) = flowResponse {
        apiCallback.requestHistoryList(token)
    }

    fun requestHistoryDetail(token: String, consultationId: String) = flowResponse {
        apiCallback.requestHistoryDetail(token, consultationId)
    }

    fun requestWriteReview(token: String, body: JsonElement) = flowResponse {
        apiCallback.requestWriteReview(token, body)
    }

    fun requestUserEdit(token: String, body: JsonElement) = flowResponse {
        apiCallback.requestUserEdit(token, body)
    }

    fun requestUserEditImage(token: String, file: File) = flowResponse {
        val requestFile = RequestBody.create(
            "image/jpg".toMediaType(),
            file
        )

        val multipartImage = MultipartBody.Part.createFormData("image", file.name, requestFile)

        apiCallback.requestUserEditImage(token, multipartImage)
    }

    fun requestUserEditBanner(token: String, file: File) = flowResponse {
        val requestFile = RequestBody.create(
            "image/jpg".toMediaType(),
            file
        )

        val multipartImage = MultipartBody.Part.createFormData("banner", file.name, requestFile)

        apiCallback.requestUserEditBanner(token, multipartImage)
    }

    fun requestChangePassword(token: String, body: JsonElement) = flowResponse {
        apiCallback.requestChangePassword(token, body)
    }
}