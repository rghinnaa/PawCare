package com.example.pawcare.ui.consultation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.pawcare.R
import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.model.DoctorDetailResponse
import com.example.pawcare.databinding.DialogConsultationConfirmationBinding
import com.example.pawcare.databinding.FragmentConsultationDetailBinding
import com.example.pawcare.ui.consultation.adapter.ReviewAdapter
import com.example.pawcare.utils.*
import com.example.pawcare.utils.liveevent.EventObserver
import com.example.pawcare.viewmodel.ConsultationViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConsultationDetailFragment : Fragment(R.layout.fragment_consultation_detail) {

    private val binding by viewBinding<FragmentConsultationDetailBinding>()
    private val viewModel by viewModels<ConsultationViewModel>()
    private val args by navArgs<ConsultationDetailFragmentArgs>()

    private var reviewAdapter = ReviewAdapter()
    private var doctorId = emptyInt

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClick()
        initFirebaseAnalytics()
        initViewModel()
        initViewModelCallback()

    }

    private fun initViewModel() {
        viewModel.requestDoctorDetail(args.id)
    }

    private fun initViewModelCallback() {
        initDoctorCallback()
        initConsultationCallback()
        initReviewCallback()
    }

    private fun initDoctorCallback() {
        viewModel.doctorDetail.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    initData(result.results?.doctorList?.firstOrNull())
                }
                is Result.Error<*> -> {}
                else -> {}
            }
        }
    }

    private fun initReviewCallback() {
        viewModel.reviews.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    if(result.results?.review.orEmpty().isNotEmpty()) {
                        binding.tvReviewTitle.isVisible = true
                        binding.rvReview.isVisible = true
                        binding.rvReview.adapter = reviewAdapter

                        reviewAdapter.differ.submitList(result.results?.review.orEmpty())
                    } else {
                        binding.tvReviewTitle.isVisible = false
                        binding.rvReview.isVisible = false
                    }
                }
                is Result.Error<*> -> {}
                else -> {}
            }
        }
    }

    private fun initConsultationCallback() {
        viewModel.consultation.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Loading -> {
                    binding.clLoading.isVisible = true
                }
                is Result.Success -> {
                    binding.clLoading.isVisible = false

                    result.results?.consultation?.firstOrNull()?.apply {
                        navController.navigateOrNull(
                            ConsultationDetailFragmentDirections.actionPaymentSummary(
                                consultationId = id.orEmpty,
                                consultationPrice = price.orEmpty,
                                paymentDue = maxPaymentTime
                            )
                        )
                    }

                    viewModel.requestConsultationNothing()
                }
                is Result.Error<*> -> {
                    binding.clLoading.isVisible = false

                    context.toast(context?.getString(R.string.error_consultation))

                    viewModel.requestConsultationNothing()
                }
                else -> {}
            }
        })
    }

    private fun initData(data: DoctorDetailResponse.Doctor?) {
        data?.let {
            doctorId = it.userDoctorDetailId.orEmpty

            binding.apply {
                ivVet.loadImage(it.banner)
                tvVet.textOrNull = it.vetName
                tvDoctor.textOrNull = it.name
                tvRating.textOrNull = it.avgRatings
                tvPatient.textOrNull = it.consultationCount.toString()
                tvLocation.textOrNull = it.address
                tvDescription.textOrNull = it.description
                if (it.discount != null) {
                    tvPrice.isVisible = true
                    tvPrice.textOrNull = it.price.asIDCurrency()
                    tvPrice.strike()
                } else {
                    tvPrice.isVisible = false
                }
                tvDiscountedPrice.textOrNull = it.discountedPrice.asIDCurrency()
            }

            viewModel.requestReview(doctorId)
        }
    }

    private fun initConfirmationDialog() {
        val dialogBinding = DialogConsultationConfirmationBinding.inflate(layoutInflater)

        context?.alertDialog(dialogBinding.root, false)?.apply {
            show()
            window?.setBackgroundDrawableResource(android.R.color.transparent)

            dialogBinding.apply {
                btnConsultation.setOnClickListener {
                    viewModel.requestConsultation(doctorId)
                    dismiss()
                }
                btnLater.setOnClickListener { dismiss() }
            }
        }
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "consultationDetail")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

    private fun initOnClick() {
        binding.btnConsultation.setOnClickListener(onClickCallback)
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.btnConsultation -> {
                initConfirmationDialog()
            }
        }
    }

}