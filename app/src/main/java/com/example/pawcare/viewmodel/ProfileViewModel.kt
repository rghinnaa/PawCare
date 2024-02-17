package com.example.pawcare.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.pawcare.base.BaseViewModel
import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.model.HistoryDetailResponse
import com.example.pawcare.data.remote.model.HistoryResponse
import com.example.pawcare.data.remote.model.UserDetailResponse
import com.example.pawcare.data.remote.repository.ConsultationRepository
import com.example.pawcare.data.remote.repository.ProfileRepository
import com.example.pawcare.utils.Const.BodyRequest.ADDRESS
import com.example.pawcare.utils.Const.BodyRequest.CONSULTATION_ID
import com.example.pawcare.utils.Const.BodyRequest.EMAIL
import com.example.pawcare.utils.Const.BodyRequest.NAME
import com.example.pawcare.utils.Const.BodyRequest.NEW_PASSWORD
import com.example.pawcare.utils.Const.BodyRequest.OLD_PASSWORD
import com.example.pawcare.utils.Const.BodyRequest.PHONE
import com.example.pawcare.utils.Const.BodyRequest.REVIEW
import com.example.pawcare.utils.Const.BodyRequest.STAR
import com.example.pawcare.utils.liveevent.Event
import com.example.pawcare.utils.toJsonElement
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    profileRepository: ProfileRepository,
    consultationRepository: ConsultationRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application) {
    private val repository = profileRepository
    private val repositoryConsultation = consultationRepository
    private val stateHandler = savedStateHandle

    var isImage = false
    var isBanner = false

    private var _userDetail: MutableLiveData<Result<UserDetailResponse>> = MutableLiveData()
    val userDetail: LiveData<Result<UserDetailResponse>> get() = _userDetail

    private var _history: MutableLiveData<Result<HistoryResponse>> = MutableLiveData()
    val history: LiveData<Result<HistoryResponse>> get() = _history

    private var _historyDetail: MutableLiveData<Result<HistoryDetailResponse>> = MutableLiveData()
    val historyDetail: LiveData<Result<HistoryDetailResponse>> get() = _historyDetail

    private var _payment: MutableLiveData<Event<Result<Any>>> = MutableLiveData()
    val payment: LiveData<Event<Result<Any>>> get() = _payment

    private var _mediaPhoto: MutableLiveData<Uri> = MutableLiveData()
    val mediaPhoto: LiveData<Uri> get() = _mediaPhoto

    private var _mediaPhotoImage: MutableLiveData<Uri> = MutableLiveData()
    val mediaPhotoImage: LiveData<Uri> get() = _mediaPhotoImage

    private var _mediaPhotoBanner: MutableLiveData<Uri> = MutableLiveData()
    val mediaPhotoBanner: LiveData<Uri> get() = _mediaPhotoBanner

    private var _writeReview: MutableLiveData<Event<Result<Any>>> = MutableLiveData()
    val writeReview: LiveData<Event<Result<Any>>> get() = _writeReview

    private var _userEdit: MutableLiveData<Event<Result<Any>>> = MutableLiveData()
    val userEdit: LiveData<Event<Result<Any>>> get() = _userEdit

    private var _userEditImage: MutableLiveData<Event<Result<Any>>> = MutableLiveData()
    val userEditImage: LiveData<Event<Result<Any>>> get() = _userEditImage

    private var _userEditBanner: MutableLiveData<Event<Result<Any>>> = MutableLiveData()
    val userEditBanner: LiveData<Event<Result<Any>>> get() = _userEditBanner

    private var _changePassword: MutableLiveData<Event<Result<Any>>> = MutableLiveData()
    val changePassword: LiveData<Event<Result<Any>>> get() = _changePassword

    fun requestUserDetail() = accessToken { token ->
        repository.requestUserDetail(token)
            .onEach { result ->
                _userDetail.value = result
            }
    }.launchIn(viewModelScope)

    fun requestHistory(status: String) = accessToken { token ->
        repository.requestHistoryList(token, status)
            .onEach { result ->
                _history.value = result
            }
    }.launchIn(viewModelScope)

    fun requestHistoryDetail(consultationId: String) = accessToken { token ->
        repository.requestHistoryDetail(token, consultationId)
            .onEach { result ->
                _historyDetail.value = result
            }
    }.launchIn(viewModelScope)

    fun requestPayment(id: String, bankName: String, sender: String, file: File) =
        accessToken { token ->
        repositoryConsultation.requestPayment(token, id, bankName, sender, file)
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

    fun onResultUriChangedImage(uri: Uri) {
        _mediaPhotoImage.value = uri
    }

    fun onResultUriChangedBanner(uri: Uri) {
        _mediaPhotoBanner.value = uri
    }

    fun requestWriteReview(id: String, star: Int, review: String) {
        val body = Gson().toJsonElement {
            put(CONSULTATION_ID, id)
            put(STAR, star)
            put(REVIEW, review)
        }

        accessToken { token ->
            repository.requestWriteReview(token, body)
                .onEach { result ->
                    _writeReview.value = Event(result)
                }
        }.launchIn(viewModelScope)
    }

    fun requestWriteReviewNothing() {
        _writeReview.value = Event(Result.nothing())
    }

    fun requestUserEdit(name: String, email: String, phone: String, address: String) {
        val body = Gson().toJsonElement {
            put(NAME, name)
            put(EMAIL, email)
            put(PHONE, phone)
            put(ADDRESS, address)
        }

        accessToken { token ->
            repository.requestUserEdit(token, body)
                .onEach { result ->
                    _userEdit.value = Event(result)
                }
        }.launchIn(viewModelScope)
    }

    fun requestUserEditNothing() {
        _userEdit.value = Event(Result.nothing())
    }

    fun requestUserEditImage(file: File) =
        accessToken { token ->
            repository.requestUserEditImage(token, file)
                .onEach { result ->
                    _userEditImage.value = Event(result)
                }
        }.launchIn(viewModelScope)

    fun requestUserEditImageNothing() {
        _userEditImage.value = Event(Result.nothing())
    }

    fun requestUserEditBanner(file: File) =
        accessToken { token ->
            repository.requestUserEditBanner(token, file)
                .onEach { result ->
                    _userEditBanner.value = Event(result)
                }
        }.launchIn(viewModelScope)

    fun requestUserEditBannerNothing() {
        _userEditBanner.value = Event(Result.nothing())
    }

    fun requestChangePassword(oldPassword: String, newPassword: String) {
        val body = Gson().toJsonElement {
            put(OLD_PASSWORD, oldPassword)
            put(NEW_PASSWORD, newPassword)
        }

        accessToken { token ->
            repository.requestChangePassword(token, body)
                .onEach { result ->
                    _changePassword.value = Event(result)
                }
        }.launchIn(viewModelScope)
    }

    fun requestChangePasswordNothing() {
        _changePassword.value = Event(Result.nothing())
    }

}