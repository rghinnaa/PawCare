package com.example.pawcare.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.pawcare.R
import com.example.pawcare.data.remote.Result
import com.example.pawcare.databinding.FragmentForgotPasswordBinding
import com.example.pawcare.utils.hideKeyboard
import com.example.pawcare.utils.liveevent.EventObserver
import com.example.pawcare.utils.navController
import com.example.pawcare.utils.orEmpty
import com.example.pawcare.utils.viewBinding
import com.example.pawcare.viewmodel.MainViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private val binding by viewBinding<FragmentForgotPasswordBinding>()
    private val viewModel by viewModels<MainViewModel>()

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClick()
        initFirebaseAnalytics()
        initForgotPasswordCallback()

    }

    private fun initForgotPasswordCallback() {
        viewModel.forgotPassword.observe(viewLifecycleOwner, EventObserver {result ->
            when(result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(context?.getString(R.string.success_forgot_password))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()

                    viewModel.requestForgotPasswordNothing()
                }
                is Result.Error<*> -> {
                    SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(context?.getString(R.string.failed_forgot_password))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()

                    viewModel.requestForgotPasswordNothing()
                }
                else -> {}
            }
        })
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "forgotPassword")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

    private fun initOnClick() {
        binding.btnSubmit.setOnClickListener(onClickCallback)
        binding.tvLogin.setOnClickListener(onClickCallback)
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.btnSubmit -> {
                binding.apply {
                    if (etEmail.text.orEmpty.isEmpty()) {
                        boxEmail.isErrorEnabled = true
                        boxEmail.error = context?.getString(R.string.error_email)
                    } else {
                        boxEmail.isErrorEnabled = false
                        boxEmail.error = null

                        viewModel.requestForgotPassword(etEmail.text.toString())
                    }
                }

                activity.hideKeyboard(view)
            }
            binding.tvLogin -> {
                navController.navigateUp()
            }
        }
    }

}