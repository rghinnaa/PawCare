package com.example.pawcare.ui.profile

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.pawcare.R
import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.model.UserDetailResponse
import com.example.pawcare.databinding.FragmentEditProfileBinding
import com.example.pawcare.utils.*
import com.example.pawcare.utils.liveevent.EventObserver
import com.example.pawcare.viewmodel.ProfileViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ProfileEditFragment : Fragment(R.layout.fragment_edit_profile) {

    private val binding by viewBinding<FragmentEditProfileBinding>()
    private val viewModel by hiltNavGraphViewModels<ProfileViewModel>(R.id.profile)

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        initViewModelCallback()

    }

    private fun initView() {
        binding.ivProfile.loadImage(R.drawable.default_profile, ImageCornerOptions.CIRCLE)
        binding.ivBanner.loadImage(R.drawable.default_banner)

        initPhoneNumberValidate()
        initOnClick()
        initFirebaseAnalytics()
    }

    private fun initViewModel() {
        viewModel.requestUserDetail()
    }

    private fun initViewModelCallback() {
        initProfileCallback()
        initMediaPhotoProfileCallback()
        initMediaPhotoBannerCallback()
        initEditUserCallback()
        initEditUserImageCallback()
        initEditUserBannerCallback()
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

    private fun initEditUserCallback() {
        viewModel.userEdit.observe(viewLifecycleOwner, EventObserver { result ->
            result.attachLoadingButton(
                button = binding.btnSubmit,
                endLoadingText = requireContext().getString(R.string.submit)
            ) {
                this.progressColor = Color.WHITE
            }

            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(context?.getString(R.string.success_user_edit))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()

                    viewModel.requestUserEditNothing()
                }
                is Result.Error<*> -> {
                    SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(context?.getString(R.string.failed_user_edit))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()

                    viewModel.requestUserEditNothing()
                }
                else -> {}
            }
        })
    }

    private fun initEditUserImageCallback() {
        viewModel.userEditImage.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {
                    binding.clLoading.isVisible = true
                }
                is Result.Success -> {
                    binding.clLoading.isVisible = false

                    SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(context?.getString(R.string.success_user_image))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()

                    viewModel.requestUserEditImageNothing()
                }
                is Result.Error<*> -> {
                    binding.clLoading.isVisible = false

                    SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(context?.getString(R.string.failed_user_image))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()

                    viewModel.requestUserEditImageNothing()
                }
                else -> {}
            }
        })
    }

    private fun initEditUserBannerCallback() {
        viewModel.userEditBanner.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {
                    binding.clLoading.isVisible = true
                }
                is Result.Success -> {
                    binding.clLoading.isVisible = false

                    SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(context?.getString(R.string.success_user_banner))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()

                    viewModel.requestUserEditBannerNothing()
                }
                is Result.Error<*> -> {
                    binding.clLoading.isVisible = false

                    SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(context?.getString(R.string.failed_user_banner))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()

                    viewModel.requestUserEditBannerNothing()
                }
                else -> {}
            }
        })
    }

    private fun initMediaPhotoProfileCallback() {
        viewModel.mediaPhotoImage.observe(viewLifecycleOwner) { uri ->
            if (viewModel.isImage) {
                uri?.copyAndGetPath(requireContext())
                    ?.fileOf()
                    ?.let { file ->
                        var compressedFile: File? = file
                        val maxPercentage = 100
                        val minusPercentage = 5
                        var currentPercentage = maxPercentage

                        run loop@{
                            repeat(maxPercentage.times(minusPercentage)) {
                                if (compressedFile?.sizeInKb.orEmpty < 3072) {
                                    binding.ivProfile.loadImage(
                                        source = compressedFile,
                                        corner = ImageCornerOptions.CIRCLE
                                    )

                                    compressedFile?.let { file ->
                                        viewModel.requestUserEditImage(file)
                                    }

                                    return@loop
                                } else {
                                    currentPercentage -= minusPercentage

                                    compressedFile = BitmapFactory.decodeFile(file.absolutePath)
                                        ?.let { bitmap ->
                                            bitmap.save(
                                                context = requireContext(),
                                                quality = currentPercentage
                                            )
                                        }
                                        ?.getFile(requireContext())
                                }
                            }
                        }
                    }
            }

            viewModel.isImage = false
        }
    }

    private fun initMediaPhotoBannerCallback() {
        viewModel.mediaPhotoBanner.observe(viewLifecycleOwner) { uri ->
            if (viewModel.isBanner) {
                uri?.copyAndGetPath(requireContext())
                    ?.fileOf()
                    ?.let { file ->
                        var compressedFile: File? = file
                        val maxPercentage = 100
                        val minusPercentage = 5
                        var currentPercentage = maxPercentage

                        run loop@{
                            repeat(maxPercentage.times(minusPercentage)) {
                                if (compressedFile?.sizeInKb.orEmpty < 3072) {
                                    binding.ivBanner.loadImage(
                                        source = compressedFile
                                    )

                                    compressedFile?.let { file ->
                                        viewModel.requestUserEditBanner(file)
                                    }

                                    return@loop
                                } else {
                                    currentPercentage -= minusPercentage

                                    compressedFile = BitmapFactory.decodeFile(file.absolutePath)
                                        ?.let { bitmap ->
                                            bitmap.save(
                                                context = requireContext(),
                                                quality = currentPercentage
                                            )
                                        }
                                        ?.getFile(requireContext())
                                }
                            }
                        }
                    }
            }

            viewModel.isBanner = false
        }
    }

    private fun initData(item: UserDetailResponse.User) {
        binding.apply {
            if (item.banner.orEmpty.isNotEmpty()) ivBanner.loadImage(item.banner)
            if (item.image.orEmpty.isNotEmpty()) ivProfile.loadImage(item.image, ImageCornerOptions.CIRCLE)
            etName.textOrNull = item.name
            etEmail.textOrNull = item.email
            etPhone.textOrNull = item.phone?.removePrefix("0")
            etAddress.textOrNull = item.address
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

    private fun initClearError() {
        binding.apply {
            boxName.isErrorEnabled = false
            boxEmail.isErrorEnabled = false
            boxPhone.isErrorEnabled = false
            boxAddress.isErrorEnabled = false

            boxName.error = null
            boxEmail.error = null
            boxPhone.error = null
            boxAddress.error = null
        }
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "editProfile")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

    private fun initOnClick() {
        binding.btnSubmit.setOnClickListener(onClickCallback)
        binding.ivProfile.setOnClickListener(onClickCallback)
        binding.ivBanner.setOnClickListener(onClickCallback)
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.btnSubmit -> {
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
                    } else if (etName.text.orEmpty.length < 2) {
                        initClearError()

                        boxName.isErrorEnabled = true
                        boxName.error = context?.getString(R.string.error_minimal_two)
                    } else {
                        initClearError()

                        viewModel.requestUserEdit(
                            etName.text.toString(),
                            etEmail.text.toString(),
                            "0${etPhone.text.toString()}",
                            etAddress.text.toString()
                        )
                    }
                }

                activity.hideKeyboard(view)
            }
            binding.ivProfile -> {
                navController.navigateOrNull(
                    ProfileEditFragmentDirections.actionAddPhoto(
                        "Ubah Foto Profil",
                        Const.ImageType.IMAGE_PROFILE
                    )
                )
            }
            binding.ivBanner -> {
                navController.navigateOrNull(
                    ProfileEditFragmentDirections.actionAddPhoto(
                        "Ubah Foto Profil",
                        Const.ImageType.IMAGE_BANNER
                    )
                )
            }
        }
    }

}