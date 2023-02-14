package com.example.pawcare.data.di

import android.app.Application
import android.content.Context
import com.example.pawcare.data.preferences.AccessManager
import com.example.pawcare.data.remote.api.ApiCallback
import com.example.pawcare.data.remote.repository.ConsultationRepository
import com.example.pawcare.data.remote.repository.HomeRepository
import com.example.pawcare.data.remote.repository.MainRepository
import com.example.pawcare.data.remote.repository.ProfileRepository
import com.example.pawcare.data.remote.source.data.ConsultationRemoteDataSource
import com.example.pawcare.data.remote.source.data.HomeRemoteDataSource
import com.example.pawcare.data.remote.source.data.MainRemoteDataSource
import com.example.pawcare.data.remote.source.data.ProfileRemoteDataSource
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideApplication(application: Application): Context = application

    @Provides
    @Singleton
    fun provideReviewManager(@ApplicationContext context: Context) =
        ReviewManagerFactory.create(context)

    @Singleton
    @Provides
    fun provideAccessManager(context: Context) = AccessManager(context)

    @Provides
    fun provideMainRepository(
        apiCallback: ApiCallback
    ) = MainRepository(
        MainRemoteDataSource(apiCallback)
    )

    @Provides
    fun provideHomeRepository(
        apiCallback: ApiCallback
    ) = HomeRepository(
        HomeRemoteDataSource(apiCallback)
    )

    @Provides
    fun provideConsultationRepository(
        apiCallback: ApiCallback
    ) = ConsultationRepository(
        ConsultationRemoteDataSource(apiCallback)
    )

    @Provides
    fun provideProfileRepository(
        apiCallback: ApiCallback
    ) = ProfileRepository(
        ProfileRemoteDataSource(apiCallback)
    )

}