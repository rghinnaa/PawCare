package com.example.pawcare.data.remote.source.data

import com.example.pawcare.data.remote.api.ApiCallback
import com.example.pawcare.utils.Const
import com.example.pawcare.utils.Const.BodyRequest.USER_DOCTOR_DETAIL_ID
import com.example.pawcare.utils.flowResponse
import com.example.pawcare.utils.toJsonElement
import com.google.gson.Gson
import com.google.gson.JsonElement
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ConsultationRemoteDataSource(callback: ApiCallback) {
    private val apiCallback = callback

    fun requestDoctor(token: String, keyword: String, items: String) = flowResponse {
        apiCallback.requestDoctor(token, keyword, items)
    }

    fun requestDoctorDetail(token: String, detailId: Int) = flowResponse {
        apiCallback.requestDoctorDetail(token, detailId)
    }

    fun requestConsultation(token: String, body: JsonElement) = flowResponse {
        apiCallback.requestConsultation(token, body)
    }

    fun requestPayment(token: String, consultationId: Int, bankName: String, senderName: String, file: File) = flowResponse {
        val requestFile = RequestBody.create(
            "image/jpg".toMediaType(),
            file
        )

        val multipartImage = MultipartBody.Part.createFormData("image", file.name, requestFile)

        apiCallback.requestPayment(token, consultationId, bankName, senderName, multipartImage)
    }

    fun requestReviews(token: String, doctorId: Int) = flowResponse {
        apiCallback.requestReviews(token, doctorId)
    }

}