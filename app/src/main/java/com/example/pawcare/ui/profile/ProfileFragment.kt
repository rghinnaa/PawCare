package com.example.pawcare.ui.profile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.pawcare.R
import com.example.pawcare.data.preferences.AccessManager
import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.model.UserDetailResponse
import com.example.pawcare.databinding.DialogLogoutConfirmationBinding
import com.example.pawcare.databinding.FragmentProfileBinding
import com.example.pawcare.ui.auth.AuthActivity
import com.example.pawcare.utils.*
import com.example.pawcare.viewmodel.MainViewModel
import com.example.pawcare.viewmodel.ProfileViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding<FragmentProfileBinding>()
    private val viewModel by viewModels<ProfileViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    @Inject
    lateinit var accessManager: AccessManager

    private var token = emptyString
    private var doubleBackToExitOnce = false

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        initViewModelCallback()

    }

    private fun initView() {
        if(token == emptyString) {
            lifecycleScope.launch {
                accessManager.access.collect {
                    token = it
                    onCheckedToken()
                }
            }
        }

        binding.ivProfile.loadImage(R.drawable.default_profile, ImageCornerOptions.CIRCLE)
        binding.ivBanner.loadImage(R.drawable.default_banner)

        initOnClick()
        onBackPressed()
        initFirebaseAnalytics()
    }

    private fun initViewModel() {
        viewModel.requestUserDetail()
    }

    private fun initViewModelCallback() {
        initProfileCallback()
        initGoToProfileCallback()
    }

    private fun initProfileCallback() {
        viewModel.userDetail.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    result.results?.userDetail?.firstOrNull()?.let { initData(it) }
                }
                is Result.Error<*> -> {}
                else -> {}
            }
        }
    }

    private fun initGoToProfileCallback() {
        mainViewModel.goToProfile.observe(viewLifecycleOwner) { result ->
            if(mainViewModel.isGoToProfile) {
                mainViewModel.isGoToHistory = true
                mainViewModel.goToHistory(result)
                navController.navigateOrNull(
                    ProfileFragmentDirections.actionHistoryConsultation()
                )

                mainViewModel.isGoToProfile = false
            }
        }
    }

    private fun initData(item: UserDetailResponse.User) {
        binding.apply {
            if (item.banner.orEmpty.isNotEmpty()) ivBanner.loadImage(item.banner)
            if (item.image.orEmpty.isNotEmpty()) ivProfile.loadImage(item.image, ImageCornerOptions.CIRCLE)
            tvName.textOrNull = item.name

            when (item.gender) {
                "Laki-Laki" -> tvName.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    context.drawable(R.drawable.ic_boy),
                    null
                )
                else -> {
                    tvName.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        context.drawable(R.drawable.ic_girl),
                        null
                    )
                }
            }
        }
    }

    private fun onCheckedToken() {
        if (token != emptyString)
            initViewModel()
        else {
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun initConfirmationDialog() {
        val dialogBinding = DialogLogoutConfirmationBinding.inflate(layoutInflater)

        context?.alertDialog(dialogBinding.root, false)?.apply {
            show()
            window?.setBackgroundDrawableResource(android.R.color.transparent)

            dialogBinding.apply {
                btnLogOut.setOnClickListener {
                    accessManager.clearAccess(lifecycleScope)

                    val intent = Intent(requireActivity(), AuthActivity::class.java)
                    startActivity(intent)
                    activity?.finish()

                    dismiss()
                }
                btnLater.setOnClickListener { dismiss() }
            }
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            if (doubleBackToExitOnce) {
                activity?.finish()
            } else {
                doubleBackToExitOnce = true
                activity.toast(context?.getString(R.string.main_exit_app))
                Handler().postDelayed({
                    kotlin.run { doubleBackToExitOnce = false }
                }, 2000)
            }
        }
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "profile")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

    private fun initOnClick() {
        binding.clWaitingPayment.setOnClickListener(onClickCallback)
        binding.clInSession.setOnClickListener(onClickCallback)
        binding.clReview.setOnClickListener(onClickCallback)
        binding.tvHistory.setOnClickListener(onClickCallback)
        binding.tvEditProfile.setOnClickListener(onClickCallback)
        binding.tvChangePassword.setOnClickListener(onClickCallback)
        binding.tvLogOut.setOnClickListener(onClickCallback)
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.clWaitingPayment -> {
                mainViewModel.isGoToHistory = true
                mainViewModel.goToHistory(0)
                navController.navigateOrNull(
                    ProfileFragmentDirections.actionHistoryConsultation()
                )
            }
            binding.clInSession -> {
                mainViewModel.isGoToHistory = true
                mainViewModel.goToHistory(3)
                navController.navigateOrNull(
                    ProfileFragmentDirections.actionHistoryConsultation()
                )
            }
            binding.clReview -> {
                mainViewModel.isGoToHistory = true
                mainViewModel.goToHistory(4)
                navController.navigateOrNull(
                    ProfileFragmentDirections.actionHistoryConsultation()
                )
            }
            binding.tvHistory -> {
                navController.navigateOrNull(
                    ProfileFragmentDirections.actionHistoryConsultation()
                )
            }
            binding.tvEditProfile -> {
                navController.navigateOrNull(
                    ProfileFragmentDirections.actionProfileEdit()
                )
            }
            binding.tvChangePassword -> {
                navController.navigateOrNull(
                    ProfileFragmentDirections.actionChangePassword()
                )
            }
            binding.tvLogOut -> {
                initConfirmationDialog()
            }
        }
    }

}