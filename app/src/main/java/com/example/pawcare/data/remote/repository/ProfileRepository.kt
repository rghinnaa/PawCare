package com.example.pawcare.data.remote.repository

import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.source.callback.ProfileSourceCallback
import com.example.pawcare.data.remote.source.data.ProfileRemoteDataSource
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import java.io.File

class ProfileRepository(
    profileRemoteDataSource: ProfileRemoteDataSource
) : ProfileSourceCallback {
    private val remoteDataSource = profileRemoteDataSource

    override fun requestUserDetail(token: String) =
        remoteDataSource.requestUserDetail(token)

    override fun requestHistoryList(token: String, status: String) =
        remoteDataSource.requestHistoryList(token, status)

    override fun requestHistoryDetail(
        token: String,
        consultationId: String
    ) = remoteDataSource.requestHistoryDetail(token, consultationId)

    override fun requestWriteReview(token: String, body: JsonElement) =
        remoteDataSource.requestWriteReview(token, body)

    override fun requestUserEdit(token: String, body: JsonElement) =
        remoteDataSource.requestUserEdit(token, body)

    override fun requestUserEditImage(token: String, file: File) =
        remoteDataSource.requestUserEditImage(token, file)

    override fun requestUserEditBanner(token: String, file: File) =
        remoteDataSource.requestUserEditBanner(token, file)

    override fun requestChangePassword(token: String, body: JsonElement) =
        remoteDataSource.requestChangePassword(token, body)
}