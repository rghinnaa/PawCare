package com.example.pawcare.data.remote.source.callback

import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.model.HistoryDetailResponse
import com.example.pawcare.data.remote.model.HistoryResponse
import com.example.pawcare.data.remote.model.UserDetailResponse
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ProfileSourceCallback {

    fun requestUserDetail(token: String): Flow<Result<UserDetailResponse>>

    fun requestHistoryList(token: String, status: String): Flow<Result<HistoryResponse>>

    fun requestHistoryDetail(token: String, consultationId: String): Flow<Result<HistoryDetailResponse>>

    fun requestWriteReview(token: String, body: JsonElement): Flow<Result<Any>>

    fun requestUserEdit(token: String, body: JsonElement): Flow<Result<Any>>

    fun requestUserEditImage(token: String, file: File): Flow<Result<Any>>

    fun requestUserEditBanner(token: String, file: File): Flow<Result<Any>>

    fun requestChangePassword(token: String, body: JsonElement): Flow<Result<Any>>

}