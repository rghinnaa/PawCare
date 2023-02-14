package com.example.pawcare.ui.profile

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.pawcare.R
import com.example.pawcare.data.remote.Result
import com.example.pawcare.databinding.FragmentChangePasswordBinding
import com.example.pawcare.utils.attachLoadingButton
import com.example.pawcare.utils.liveevent.EventObserver
import com.example.pawcare.utils.navController
import com.example.pawcare.utils.orEmpty
import com.example.pawcare.utils.viewBinding
import com.example.pawcare.viewmodel.ProfileViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    private val binding by viewBinding<FragmentChangePasswordBinding>()
    private val viewModel by viewModels<ProfileViewModel>()

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClick()
        initFirebaseAnalytics()
        initChangePasswordCallback()

    }

    private fun initChangePasswordCallback() {
        viewModel.changePassword.observe(viewLifecycleOwner, EventObserver { result ->
            result.attachLoadingButton(
                button = binding.btnSubmit,
                endLoadingText = requireContext().getString(R.string.submit)
            ) {
                this.progressColor = Color.WHITE
            }

            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    if(result.status == "FAIL") {
                        SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(result.message)
                            .setConfirmClickListener {
                                it.dismissWithAnimation()
                            }
                            .show()
                    } else {
                        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(context?.getString(R.string.success_change_password))
                            .setConfirmClickListener {
                                it.dismissWithAnimation()
                                navController.navigateUp()
                            }
                            .show()
                    }

                    viewModel.requestChangePasswordNothing()
                }
                is Result.Error<*> -> {
                    SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(context?.getString(R.string.failed_change_password))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()

                    viewModel.requestChangePasswordNothing()
                }
                else -> {}
            }
        })
    }

    private fun initClearError() {
        binding.apply {
            boxOldPassword.isErrorEnabled = false
            boxNewPassword.isErrorEnabled = false

            boxOldPassword.error = null
            boxNewPassword.error = null
        }
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "changePassword")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

    private fun initOnClick() {
        binding.btnSubmit.setOnClickListener(onClickCallback)
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.btnSubmit -> {
                binding.apply {
                    if (etOldPassword.text.orEmpty.isEmpty()) {
                        initClearError()

                        boxOldPassword.isErrorEnabled = true
                        boxOldPassword.error = context?.getString(R.string.error_old_password)
                    } else if (etNewPassword.text.orEmpty.isEmpty()) {
                        initClearError()

                        boxNewPassword.isErrorEnabled = true
                        boxNewPassword.error = context?.getString(R.string.error_new_password)
                    } else if (etNewPassword.text.orEmpty.length < 8) {
                        initClearError()

                        boxNewPassword.isErrorEnabled = true
                        boxNewPassword.error = context?.getString(R.string.error_minimal_eight)
                    } else {
                        viewModel.requestChangePassword(
                            etOldPassword.text.toString(),
                            etNewPassword.text.toString()
                        )
                    }
                }
            }
        }
    }

}