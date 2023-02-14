package com.example.pawcare.ui.auth

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pawcare.R
import com.example.pawcare.data.remote.Result
import com.example.pawcare.databinding.FragmentLoginBinding
import com.example.pawcare.utils.*
import com.example.pawcare.viewmodel.MainViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding<FragmentLoginBinding>()
    private val viewModel by viewModels<MainViewModel>()

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClick()
        initFirebaseAnalytics()
        initViewModelCallback()

    }

    private fun initViewModel() {
        viewModel.requestLogin(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
    }

    private fun initViewModelCallback() {
        initLoginCallback()
    }

    private fun initLoginCallback() {
        viewModel.login.observe(viewLifecycleOwner) {result ->
            result.attachLoadingButton(
                button = binding.btnLogin,
                endLoadingText = requireContext().getString(R.string.login)
            ) {
                this.progressColor = Color.WHITE
            }

            when(result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    if(result.status == "FAIL") {
                        context?.toast(context?.getString(R.string.error_login))
                    }
                }
                is Result.Error<*> -> {}
                else -> {}
            }
        }

        viewModel.token.observe(viewLifecycleOwner) {
            if (it.length > 8) {
                navController.navigateOrNull(
                    LoginFragmentDirections.actionMainFragment()
                )
            }
        }
    }

    private fun initClearError() {
        binding.apply {
            boxEmail.isErrorEnabled = false
            boxPassword.isErrorEnabled = false

            boxEmail.error = null
            boxPassword.error = null
        }
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "login")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

    private fun initOnClick() {
        binding.btnLogin.setOnClickListener(onClickCallback)
        binding.tvRegister.setOnClickListener(onClickCallback)
        binding.tvForgotPassword.setOnClickListener(onClickCallback)
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.btnLogin -> {
                binding.apply {
                    if(etEmail.text.orEmpty.isEmpty()) {
                        initClearError()

                        boxEmail.isErrorEnabled = true
                        boxEmail.error = context?.getString(R.string.error_email)
                    } else if(etPassword.text.orEmpty.isEmpty()) {
                        initClearError()

                        boxPassword.isErrorEnabled = true
                        boxPassword.error = context?.getString(R.string.error_password)
                    } else {
                        initViewModel()
                    }
                    activity.hideKeyboard(view)
                }
            }
            binding.tvRegister -> {
                navController.navigateOrNull(
                    LoginFragmentDirections.actionRegisterFragment()
                )
            }
            binding.tvForgotPassword -> {
                navController.navigateOrNull(
                    LoginFragmentDirections.actionForgotPasswordFragment()
                )
            }
        }
    }

}