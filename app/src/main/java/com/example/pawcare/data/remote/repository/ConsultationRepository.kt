package com.example.pawcare.data.remote.repository

import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.model.ReviewResponse
import com.example.pawcare.data.remote.source.callback.ConsultationSourceCallback
import com.example.pawcare.data.remote.source.data.ConsultationRemoteDataSource
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import java.io.File

class ConsultationRepository(
    consultationRemoteDataSource: ConsultationRemoteDataSource
) : ConsultationSourceCallback {
    private val remoteDataSource = consultationRemoteDataSource

    override fun requestDoctor(token: String, keyword: String, items: String) =
        remoteDataSource.requestDoctor(token, keyword, items)

    override fun requestDoctorDetail(
        token: String,
        detailId: Int
    ) = remoteDataSource.requestDoctorDetail(token, detailId)

    override fun requestConsultation(
        token: String,
        body: JsonElement
    ) = remoteDataSource.requestConsultation(token, body)

    override fun requestPayment(
        token: String,
        consultationId: Int,
        bankName: String,
        senderName: String,
        file: File
    ) = remoteDataSource.requestPayment(token, consultationId, bankName, senderName, file)

    override fun requestReviews(token: String, doctorId: Int) =
        remoteDataSource.requestReviews(token, doctorId)
}