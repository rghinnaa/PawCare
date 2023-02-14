package com.example.pawcare.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.pawcare.base.BaseViewModel
import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.model.BannerResponse
import com.example.pawcare.data.remote.model.DoctorResponse
import com.example.pawcare.data.remote.repository.HomeRepository
import com.example.pawcare.utils.emptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    homeRepository: HomeRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {
    private val repository = homeRepository
    private val stateHandler = savedStateHandle

    private var _bannerList: MutableLiveData<Result<BannerResponse>> = MutableLiveData()
    val bannerList: LiveData<Result<BannerResponse>> get() = _bannerList

    private var _doctorList: MutableLiveData<Result<DoctorResponse>> = MutableLiveData()
    val doctorList: LiveData<Result<DoctorResponse>> get() = _doctorList

    fun requestBannerList() = accessToken { token ->
        repository.requestBanner(token)
            .onEach { result ->
                _bannerList.value = result
            }
    }.launchIn(viewModelScope)

    fun requestDoctorList(keyword: String = emptyString, items: String = emptyString) =
        accessToken { token ->
            repository.requestDoctor(token, keyword, items)
                .onEach { result ->
                    _doctorList.value = result
                }
        }.launchIn(viewModelScope)

}