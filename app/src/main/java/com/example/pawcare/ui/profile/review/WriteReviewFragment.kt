package com.example.pawcare.ui.profile.review

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.pawcare.R
import com.example.pawcare.data.remote.Result
import com.example.pawcare.databinding.FragmentWriteReviewBinding
import com.example.pawcare.utils.*
import com.example.pawcare.utils.liveevent.EventObserver
import com.example.pawcare.viewmodel.ProfileViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteReviewFragment : Fragment(R.layout.fragment_write_review) {

    private val binding by viewBinding<FragmentWriteReviewBinding>()
    private val viewModel by viewModels<ProfileViewModel>()
    private val args by navArgs<WriteReviewFragmentArgs>()

    private var star = emptyInt

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModelCallback()

    }

    private fun initView() {
        initData()
        initDescription()
        initOnClick()
        initFirebaseAnalytics()
    }

    private fun initViewModelCallback() {
        initWriteReviewCallback()
    }

    private fun initWriteReviewCallback() {
        viewModel.writeReview.observe(viewLifecycleOwner, EventObserver { result ->
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
                        .setTitleText(context?.getString(R.string.success_review))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                            findNavController().navigateUp()
                        }
                        .show()

                    viewModel.requestWriteReviewNothing()
                }
                is Result.Error<*> -> {
                    SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(context?.getString(R.string.failed_review))
                        .setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        .show()

                    viewModel.requestWriteReviewNothing()
                }
                else -> {}
            }
        })
    }

    private fun initData() {
        binding.apply {
            tvVetName.textOrNull = args.vetName
            tvDoctorName.textOrNull = args.doctorName
            tvDiscountedPrice.textOrNull = args.discountedPrice
            if(args.price.orEmpty.isNotEmpty()) {
                tvPrice.isVisible = true
                tvPrice.textOrNull = args.price
                tvPrice.strike()
            } else {
                tvPrice.isVisible = false
            }
            tvDate.textOrNull = args.date
        }
    }

    private fun initDescription() {
        binding.boxReview.editText?.addDelayOnTypeWithScope(100, lifecycleScope) {
            binding.tvMax.textOrNull = "${binding.etReview.text.toString().length}/500"
        }
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "writeReview")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

    private fun initOnClick() {
        binding.btnSubmit.setOnClickListener(onClickCallback)
        binding.ivStar1.setOnClickListener(onClickCallback)
        binding.ivStar2.setOnClickListener(onClickCallback)
        binding.ivStar3.setOnClickListener(onClickCallback)
        binding.ivStar4.setOnClickListener(onClickCallback)
        binding.ivStar5.setOnClickListener(onClickCallback)
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.btnSubmit -> {
                if(star < 1) {
                    context?.toast(context?.getString(R.string.warning_star))
                } else if (binding.etReview.text.toString().isEmpty()) {
                    context?.toast(context?.getString(R.string.warning_review))
                } else {
                    viewModel.requestWriteReview(
                        args.consultationId,
                        star,
                        binding.etReview.text.toString()
                    )
                }
            }
            binding.ivStar1 -> {
                binding.apply {
                    ivStar1.loadImage(R.drawable.ic_review_fill)
                    ivStar2.loadImage(R.drawable.ic_review)
                    ivStar3.loadImage(R.drawable.ic_review)
                    ivStar4.loadImage(R.drawable.ic_review)
                    ivStar5.loadImage(R.drawable.ic_review)

                    star = 1
                }
            }
            binding.ivStar2 -> {
                binding.apply {
                    ivStar1.loadImage(R.drawable.ic_review_fill)
                    ivStar2.loadImage(R.drawable.ic_review_fill)
                    ivStar3.loadImage(R.drawable.ic_review)
                    ivStar4.loadImage(R.drawable.ic_review)
                    ivStar5.loadImage(R.drawable.ic_review)

                    star = 2
                }
            }
            binding.ivStar3 -> {
                binding.apply {
                    ivStar1.loadImage(R.drawable.ic_review_fill)
                    ivStar2.loadImage(R.drawable.ic_review_fill)
                    ivStar3.loadImage(R.drawable.ic_review_fill)
                    ivStar4.loadImage(R.drawable.ic_review)
                    ivStar5.loadImage(R.drawable.ic_review)

                    star = 3
                }
            }
            binding.ivStar4 -> {
                binding.apply {
                    ivStar1.loadImage(R.drawable.ic_review_fill)
                    ivStar2.loadImage(R.drawable.ic_review_fill)
                    ivStar3.loadImage(R.drawable.ic_review_fill)
                    ivStar4.loadImage(R.drawable.ic_review_fill)
                    ivStar5.loadImage(R.drawable.ic_review)

                    star = 4
                }
            }
            binding.ivStar5 -> {
                binding.apply {
                    ivStar1.loadImage(R.drawable.ic_review_fill)
                    ivStar2.loadImage(R.drawable.ic_review_fill)
                    ivStar3.loadImage(R.drawable.ic_review_fill)
                    ivStar4.loadImage(R.drawable.ic_review_fill)
                    ivStar5.loadImage(R.drawable.ic_review_fill)

                    star = 5
                }
            }
        }
    }

}