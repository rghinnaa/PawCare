package com.example.pawcare.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.pawcare.R
import com.example.pawcare.data.remote.Result
import com.example.pawcare.databinding.FragmentRegisterBinding
import com.example.pawcare.utils.*
import com.example.pawcare.utils.liveevent.EventObserver
import com.example.pawcare.viewmodel.MainViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding<FragmentRegisterBinding>()
    private val viewModel by viewModels<MainViewModel>()

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModelCallback()

    }

    private fun initView() {
        initOnClick()
        initPhoneNumberValidate()
        initFirebaseAnalytics()
    }

    private fun initViewModelCallback() {
        initRegisterCallback()
    }

    private fun initRegisterCallback() {
        viewModel.register.observe(viewLifecycleOwner, EventObserver { result ->
            result.attachLoadingButton(
                button = binding.btnRegister,
                endLoadingText = requireContext().getString(R.string.register)
            ) {
                this.progressColor = Color.WHITE
            }

            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(context?.getString(R.string.success_register))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                            findNavController().navigateUp()
                        }
                        .show()

                    viewModel.requestRegisterNothing()
                }
                is Result.Error<*> -> {
                    SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(context?.getString(R.string.failed_register))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()

                    viewModel.requestRegisterNothing()
                }
                else -> {}
            }
        })
    }

    private fun initValidateRegister() {
        binding.apply {
            if (etName.text.orEmpty.isEmpty()) {
                initClearError()

                boxName.isErrorEnabled = true
                boxName.error = context?.getString(R.string.error_name)
            } else if (etEmail.text.orEmpty.isEmpty()) {
                initClearError()

                boxEmail.isErrorEnabled = true
                boxEmail.error = context?.getString(R.string.error_email)
            } else if (etPhone.text.orEmpty.isEmpty()) {
                initClearError()

                boxPhone.isErrorEnabled = true
                boxPhone.error = context?.getString(R.string.error_phone)
            } else if (etAddress.text.orEmpty.isEmpty()) {
                initClearError()

                boxAddress.isErrorEnabled = true
                boxAddress.error = context?.getString(R.string.error_address)
            } else if (etPassword.text.orEmpty.isEmpty()) {
                initClearError()

                boxPassword.isErrorEnabled = true
                boxPassword.error = context?.getString(R.string.error_password)
            } else if (etConfirmPassword.text.orEmpty.isEmpty()) {
                initClearError()

                boxConfirmPassword.isErrorEnabled = true
                boxConfirmPassword.error = context?.getString(R.string.error_confirm_password)
            } else if (etName.text.orEmpty.length < 2) {
                initClearError()

                boxName.isErrorEnabled = true
                boxName.error = context?.getString(R.string.error_minimal_two)
            } else if (etPassword.text.orEmpty.length < 8) {
                initClearError()

                boxPassword.isErrorEnabled = true
                boxPassword.error = context?.getString(R.string.error_minimal_eight)
            } else if (etConfirmPassword.text.toString() != etPassword.text.toString()) {
                initClearError()

                boxConfirmPassword.isErrorEnabled = true
                boxConfirmPassword.error = context?.getString(R.string.error_password_not_match)
            } else if (rgGender.checkedRadioButtonId == emptyInt) {
                initClearError()

                context?.toast(context?.getString(R.string.error_gender))
            } else {
                initClearError()

                viewModel.requestRegister(
                    etName.text.toString(),
                    etEmail.text.toString(),
                    "0${etPhone.text.toString()}",
                    etAddress.text.toString(),
                    if (rbBoy.isChecked) rbBoy.text.toString()
                    else rbGirl.text.toString(),
                    etPassword.text.toString()
                )
            }
        }
    }

    private fun initClearError() {
        binding.apply {
            boxName.isErrorEnabled = false
            boxEmail.isErrorEnabled = false
            boxPhone.isErrorEnabled = false
            boxAddress.isErrorEnabled = false
            boxPassword.isErrorEnabled = false
            boxConfirmPassword.isErrorEnabled = false

            boxName.error = null
            boxEmail.error = null
            boxPhone.error = null
            boxAddress.error = null
            boxPassword.error = null
            boxConfirmPassword.error = null
        }
    }

    private fun initPhoneNumberValidate() {
        binding.boxPhone.editText?.addDelayOnTypeWithScope(200, lifecycleScope) {
            if (it.length == 1 && it.startsWith('0')) {
                binding.etPhone.setText("")
            } else if(it.length > 1 && it.startsWith('0')) {
                binding.etPhone.setText(
                    it.substring(1, (it.length.orEmpty))
                )
            }
        }
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "register")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

    private fun initOnClick() {
        binding.btnRegister.setOnClickListener(onClickCallback)
        binding.tvLogin.setOnClickListener(onClickCallback)
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.btnRegister -> {
                initValidateRegister()
                activity.hideKeyboard(view)
            }
            binding.tvLogin -> {
                navController.navigateUp()
            }
        }
    }

}