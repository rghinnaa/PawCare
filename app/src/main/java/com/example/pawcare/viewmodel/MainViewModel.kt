package com.example.pawcare.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.pawcare.base.BaseViewModel
import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.repository.MainRepository
import com.example.pawcare.utils.Const.BodyRequest.ADDRESS
import com.example.pawcare.utils.Const.BodyRequest.GENDER
import com.example.pawcare.utils.Const.BodyRequest.NAME
import com.example.pawcare.utils.Const.BodyRequest.PHONE
import com.example.pawcare.utils.Const.Login.EMAIL
import com.example.pawcare.utils.Const.Login.PASSWORD
import com.example.pawcare.utils.liveevent.Event
import com.example.pawcare.utils.toJsonElement
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    mainRepository: MainRepository,
    savedStateHandle: SavedStateHandle
): BaseViewModel(application) {
    private val repository = mainRepository
    private val stateHandler = savedStateHandle

    var isGoToDetail = false
    var isGoToHistory = false
    var isGoToProfile = false

    private var _token: MutableLiveData<String> = MutableLiveData()
    val token: LiveData<String> get() = _token

    private var _login: MutableLiveData<Result<Any>> = MutableLiveData()
    val login: LiveData<Result<Any>> get() = _login

    private var _register: MutableLiveData<Event<Result<Any>>> = MutableLiveData()
    val register: LiveData<Event<Result<Any>>> get() = _register

    private var _forgotPassword: MutableLiveData<Event<Result<Any>>> = MutableLiveData()
    val forgotPassword: LiveData<Event<Result<Any>>> get() = _forgotPassword

    private var _goToDetail: MutableLiveData<Int> = MutableLiveData()
    val goToDetail: LiveData<Int> get() = _goToDetail

    private var _goToHistory: MutableLiveData<Int> = MutableLiveData()
    val goToHistory: LiveData<Int> get() = _goToHistory

    private var _goToProfile: MutableLiveData<Int> = MutableLiveData()
    val goToProfile: LiveData<Int> get() = _goToProfile

    private fun checkIfUserAlreadyLoggedIn() = accessTokenFlow
        .onEach { access -> _token.value = access }
        .launchIn(viewModelScope)

    fun requestLogin(email: String, password: String) {
        val body = Gson().toJsonElement {
            put(EMAIL, email)
            put(PASSWORD, password)
        }
        repository.requestLogin(body).onEach { result ->
            _login.value = result

            if(result is Result.Success) {
                if(result.status != "FAIL") {
                    val token = result.results.toString()
                    accessManager.setAccess(token)
                    _token.value = token
                }
            }
        }.launchIn(viewModelScope)
    }

    fun requestRegister(
        name: String,
        email: String,
        phone: String,
        address: String,
        gender: String,
        password: String
    ) {
        val body = Gson().toJsonElement {
            put(NAME, name)
            put(EMAIL, email)
            put(PASSWORD, password)
            put(GENDER, gender)
            put(ADDRESS, address)
            put(PHONE, phone)
        }

        repository.requestRegister(body).onEach { result ->
            _register.value = Event(result)
        }.launchIn(viewModelScope)
    }

    fun requestRegisterNothing() {
        _register.value = Event(Result.nothing())
    }

    fun requestForgotPassword(email: String) {
        val body = Gson().toJsonElement {
            put(EMAIL, email)
        }

        repository.requestForgotPassword(body).onEach { result ->
            _forgotPassword.value = Event(result)
        }.launchIn(viewModelScope)
    }

    fun requestForgotPasswordNothing() {
        _forgotPassword.value = Event(Result.nothing())
    }

    fun goToDetail(doctorId: Int) {
        _goToDetail.value = doctorId
    }

    fun goToHistory(position: Int) {
        _goToHistory.value = position
    }

    fun goToProfile(position: Int) {
        _goToProfile.value = position
    }

    init {
        checkIfUserAlreadyLoggedIn()
    }

}