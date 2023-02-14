package com.example.pawcare.data.remote.repository

import com.example.pawcare.data.remote.source.callback.HomeSourceCallback
import com.example.pawcare.data.remote.source.data.HomeRemoteDataSource

class HomeRepository(
    homeRemoteDataSource: HomeRemoteDataSource
) : HomeSourceCallback {
    private val remoteDataSource = homeRemoteDataSource

    override fun requestBanner(token: String) = remoteDataSource.requestBanner(token)

    override fun requestDoctor(token: String, keyword: String, items: String) =
        remoteDataSource.requestDoctor(token, keyword, items)
}