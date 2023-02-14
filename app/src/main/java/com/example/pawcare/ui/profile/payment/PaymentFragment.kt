package com.example.pawcare.ui.profile.payment

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.pawcare.R
import com.example.pawcare.data.remote.Result
import com.example.pawcare.databinding.FragmentPaymentBinding
import com.example.pawcare.utils.*
import com.example.pawcare.utils.liveevent.EventObserver
import com.example.pawcare.viewmodel.ProfileViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private val binding by viewBinding<FragmentPaymentBinding>()
    private val viewModel by hiltNavGraphViewModels<ProfileViewModel>(R.id.profile)
    private val args by navArgs<PaymentFragmentArgs>()

    private var imageFile: File? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClick()
        initFirebaseAnalytics()
        initViewModelCallback()

    }

    private fun initViewModelCallback() {
        initMediaPhotoCallback()
        initPaymentCallback()
    }

    private fun initMediaPhotoCallback() {
        viewModel.mediaPhoto.observe(viewLifecycleOwner) { uri ->
            binding.clUploadPlaceholder.isVisible = false

            uri?.copyAndGetPath(requireContext())
                ?.fileOf()
                ?.let { file ->
                    var compressedFile: File? = file
                    val maxPercentage = 100
                    val minusPercentage = 5
                    var currentPercentage = maxPercentage

                    run loop@ {
                        repeat(maxPercentage.times(minusPercentage)) {
                            if (compressedFile?.sizeInKb.orEmpty < 3072) {
                                binding.ivUploadImage.loadImage(
                                    source = compressedFile,
                                    corner = ImageCornerOptions.ROUNDED
                                )

                                imageFile = compressedFile

                                return@loop
                            } else {
                                currentPercentage -= minusPercentage

                                compressedFile = BitmapFactory.decodeFile(file.absolutePath)
                                    ?.let { bitmap ->
                                        bitmap.save(
                                            context = requireContext(),
                                            quality = currentPercentage
                                        ) }
                                    ?.getFile(requireContext())
                            }
                        }
                    }
                }
        }
    }

    private fun initPaymentCallback() {
        viewModel.payment.observe(viewLifecycleOwner, EventObserver { result ->
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
                        .setTitleText(context?.getString(R.string.success_upload))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                            findNavController().navigateUp()
                        }
                        .show()

                    viewModel.requestPaymentNothing()
                }
                is Result.Error<*> -> {
                    SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(context?.getString(R.string.failed_upload))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()

                    viewModel.requestPaymentNothing()
                }
                else -> {}
            }
        })
    }

    private fun initClearError() {
        binding.apply {
            boxBank.isErrorEnabled = false
            boxName.isErrorEnabled = false

            boxBank.error = null
            boxName.error = null
        }
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "paymentProfile")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

    private fun initOnClick() {
        binding.vUploadImage.setOnClickListener(onClickCallback)
        binding.btnSubmit.setOnClickListener(onClickCallback)
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.vUploadImage -> {
                navController.navigateOrNull(
                    PaymentFragmentDirections.actionAddPhoto(
                        context?.getString(R.string.label_upload_payment)
                    )
                )
            }
            binding.btnSubmit -> {
                if(binding.etBank.text.orEmpty.isEmpty()) {
                    initClearError()

                    binding.boxBank.isErrorEnabled = true
                    binding.boxBank.error = context?.getString(R.string.error_bank)
                } else if(binding.etName.text.orEmpty.isEmpty()) {
                    initClearError()

                    binding.boxName.isErrorEnabled = true
                    binding.boxName.error = context?.getString(R.string.error_bank_name)
                } else if(imageFile == null) {
                    context?.toast(context?.getString(R.string.error_image))
                } else {
                    imageFile?.let {
                        viewModel.requestPayment(
                            args.consultationId,
                            binding.etBank.text.toString(),
                            binding.etName.text.toString(),
                            it
                        )
                    }
                }
            }
        }
    }

}