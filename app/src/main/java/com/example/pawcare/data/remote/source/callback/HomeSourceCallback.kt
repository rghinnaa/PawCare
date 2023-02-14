package com.example.pawcare.data.remote.source.callback

import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.model.BannerResponse
import com.example.pawcare.data.remote.model.DoctorResponse
import kotlinx.coroutines.flow.Flow

interface HomeSourceCallback {

    fun requestBanner(token: String): Flow<Result<BannerResponse>>

    fun requestDoctor(token: String, keyword: String, items: String): Flow<Result<DoctorResponse>>

}