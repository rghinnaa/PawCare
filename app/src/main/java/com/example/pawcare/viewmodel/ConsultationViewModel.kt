package com.example.pawcare.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.pawcare.base.BaseViewModel
import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.model.ConsultationResponse
import com.example.pawcare.data.remote.model.DoctorDetailResponse
import com.example.pawcare.data.remote.model.DoctorResponse
import com.example.pawcare.data.remote.model.ReviewResponse
import com.example.pawcare.data.remote.repository.ConsultationRepository
import com.example.pawcare.utils.Const.BodyRequest.USER_DOCTOR_DETAIL_ID
import com.example.pawcare.utils.emptyString
import com.example.pawcare.utils.liveevent.Event
import com.example.pawcare.utils.toJsonElement
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ConsultationViewModel @Inject constructor(
    application: Application,
    consultationRepository: ConsultationRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {
    private val repository = consultationRepository
    private val stateHandler = savedStateHandle

    private var _doctorList: MutableLiveData<Result<DoctorResponse>> = MutableLiveData()
    val doctorList: LiveData<Result<DoctorResponse>> get() = _doctorList

    private var _doctorDetail: MutableLiveData<Result<DoctorDetailResponse>> = MutableLiveData()
    val doctorDetail: LiveData<Result<DoctorDetailResponse>> get() = _doctorDetail

    private var _consultation: MutableLiveData<Event<Result<ConsultationResponse>>> =
        MutableLiveData()
    val consultation: LiveData<Event<Result<ConsultationResponse>>> get() = _consultation

    private var _payment: MutableLiveData<Event<Result<Any>>> = MutableLiveData()
    val payment: LiveData<Event<Result<Any>>> get() = _payment

    private var _mediaPhoto: MutableLiveData<Uri> = MutableLiveData()
    val mediaPhoto: LiveData<Uri> get() = _mediaPhoto

    private var _reviews: MutableLiveData<Result<ReviewResponse>> = MutableLiveData()
    val reviews: LiveData<Result<ReviewResponse>> get() = _reviews

    fun requestDoctorList(keyword: String = emptyString, items: String = emptyString) =
        accessToken { token ->
            repository.requestDoctor(token, keyword, items)
                .onEach { result ->
                    _doctorList.value = result
                }
        }.launchIn(viewModelScope)

    fun requestDoctorDetail(detailId: String) =
        accessToken { token ->
            repository.requestDoctorDetail(token, detailId)
                .onEach { result ->
                    _doctorDetail.value = result
                }
        }.launchIn(viewModelScope)

    fun requestConsultation(doctorId: String) {
        val body = Gson().toJsonElement {
            put(USER_DOCTOR_DETAIL_ID, doctorId)
        }

        accessToken { token ->
            repository.requestConsultation(token, body)
                .onEach { result ->
                    _consultation.value = Event(result)
                }
        }.launchIn(viewModelScope)
    }

    fun requestConsultationNothing() {
        _consultation.value = Event(Result.nothing())
    }

    fun requestPayment(id: String, bankName: String, sender: String, file: File) =
        accessToken { token ->
            repository.requestPayment(token, id, bankName, sender, file)
                .onEach { result ->
                    _payment.value = Event(result)
                }
        }.launchIn(viewModelScope)

    fun requestPaymentNothing() {
        _payment.value = Event(Result.nothing())
    }

    fun onResultUriChanged(uri: Uri) {
        _mediaPhoto.value = uri
    }

    fun requestReview(doctorId: String) =
        accessToken { token ->
            repository.requestReviews(token, doctorId)
                .onEach { result ->
                    _reviews.value = result
                }
        }.launchIn(viewModelScope)

}