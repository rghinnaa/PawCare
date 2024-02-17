package com.example.pawcare.data.remote.api

import com.example.pawcare.base.BaseResponse
import com.example.pawcare.data.remote.model.*
import com.example.pawcare.utils.Const
import com.google.gson.JsonElement
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiCallback {

    @POST(Const.Network.LOGIN)
    suspend fun requestLogin(
        @Body body: JsonElement
    ): Response<BaseResponse<Any>>

    @POST(Const.Network.REGISTER)
    suspend fun requestRegister(
        @Body body: JsonElement
    ): Response<BaseResponse<Any>>

    @POST(Const.Network.FORGOT_PASSWORD)
    suspend fun requestForgotPassword(
        @Body body: JsonElement
    ): Response<BaseResponse<Any>>

    @GET(Const.Network.BANNER_LIST)
    suspend fun requestBanner(
        @Header("Authorization") token: String
    ): Response<BaseResponse<BannerResponse>>

    @GET(Const.Network.DOCTOR_LIST)
    suspend fun requestDoctor(
        @Header("Authorization") token: String,
        @Query("search") keyword: String,
        @Query("items") items: String
    ): Response<BaseResponse<DoctorResponse>>

    @GET(Const.Network.DOCTOR_DETAIL)
    suspend fun requestDoctorDetail(
        @Header("Authorization") token: String,
        @Path("id") detailId: String
    ): Response<BaseResponse<DoctorDetailResponse>>

    @POST(Const.Network.CONSULTATION)
    suspend fun requestConsultation(
        @Header("Authorization") token: String,
        @Body body: JsonElement
    ): Response<BaseResponse<ConsultationResponse>>

    @Multipart
    @POST(Const.Network.PAYMENT)
    suspend fun requestPayment(
        @Header("Authorization") token: String,
        @Part("consultation_id") consultationId: String,
        @Part("bank_name") bankName: String,
        @Part("sender_name") senderName: String,
        @Part file: MultipartBody.Part
    ): Response<BaseResponse<Any>>

    @GET(Const.Network.HISTORY_LIST)
    suspend fun requestHistoryList(
        @Header("Authorization") token: String,
//        @Query("search") status: String,
    ): Response<BaseResponse<HistoryResponse>>

    @GET(Const.Network.HISTORY_DETAIL)
    suspend fun requestHistoryDetail(
        @Header("Authorization") token: String,
        @Path("consultation_id") consultationId: String,
    ): Response<BaseResponse<HistoryDetailResponse>>

    @GET(Const.Network.USER_DETAIL)
    suspend fun requestUserDetail(
        @Header("Authorization") token: String
    ): Response<BaseResponse<UserDetailResponse>>

    @POST(Const.Network.WRITE_REVIEW)
    suspend fun requestWriteReview(
        @Header("Authorization") token: String,
        @Body body: JsonElement
    ): Response<BaseResponse<Any>>

    @POST(Const.Network.USER_EDIT)
    suspend fun requestUserEdit(
        @Header("Authorization") token: String,
        @Body body: JsonElement
    ): Response<BaseResponse<Any>>

    @Multipart
    @POST(Const.Network.USER_EDIT_PROFILE)
    suspend fun requestUserEditImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Response<BaseResponse<Any>>

    @Multipart
    @POST(Const.Network.USER_EDIT_BANNER)
    suspend fun requestUserEditBanner(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Response<BaseResponse<Any>>

    @POST(Const.Network.USER_EDIT_PASSWORD)
    suspend fun requestChangePassword(
        @Header("Authorization") token: String,
        @Body body: JsonElement
    ): Response<BaseResponse<Any>>

    @GET(Const.Network.REVIEWS)
    suspend fun requestReviews(
        @Header("Authorization") token: String,
        @Path("_id") doctorId: String
    ): Response<BaseResponse<ReviewResponse>>
}