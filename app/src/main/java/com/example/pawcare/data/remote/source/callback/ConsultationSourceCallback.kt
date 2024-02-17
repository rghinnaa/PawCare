package com.example.pawcare.data.remote.source.callback

import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.model.*
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ConsultationSourceCallback {

    fun requestDoctor(token: String, keyword: String, items: String): Flow<Result<DoctorResponse>>

    fun requestDoctorDetail(token: String, detailId: String): Flow<Result<DoctorDetailResponse>>

    fun requestConsultation(token: String, body: JsonElement): Flow<Result<ConsultationResponse>>

    fun requestPayment(token: String, consultationId: String, bankName: String, senderName: String, file: File): Flow<Result<Any>>

    fun requestReviews(token: String, doctorId: String): Flow<Result<ReviewResponse>>

}