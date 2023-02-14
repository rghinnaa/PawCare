package com.example.pawcare.ui.profile.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.pawcare.R
import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.model.HistoryDetailResponse
import com.example.pawcare.databinding.FragmentHistoryConsultationDetailBinding
import com.example.pawcare.ui.consultation.ConsultationDetailFragmentDirections
import com.example.pawcare.utils.*
import com.example.pawcare.viewmodel.ProfileViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HistoryConsultationDetailFragment : Fragment(R.layout.fragment_history_consultation_detail) {

    private val binding by viewBinding<FragmentHistoryConsultationDetailBinding>()
    private val viewModel by viewModels<ProfileViewModel>()
    private val args by navArgs<HistoryConsultationDetailFragmentArgs>()

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    private val formatDateTime = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US)
    private val formatDate = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    private var consultationId = emptyInt
    private var whatsapp = emptyString
    private var vetName = emptyString
    private var doctorName = emptyString
    private var price = emptyString
    private var discountedPrice = emptyString
    private var discountedPrices = emptyInt
    private var date = emptyString
    private var maxPaymentTime = emptyString

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClick()
        initFirebaseAnalytics()
        initViewModel()
        initViewModelCallback()

    }

    private fun initViewModel() {
        viewModel.requestHistoryDetail(args.id)
    }

    private fun initViewModelCallback() {
        initHistoryDetailCallback()
    }

    private fun initHistoryDetailCallback() {
        viewModel.historyDetail.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.btnConsultation.isEnabled = false}
                is Result.Success -> {
                    result.results?.consultation?.firstOrNull()?.let { initData(it) }
                    binding.btnConsultation.isEnabled = true
                }
                is Result.Error<*> -> {}
                else -> {}
            }
        }
    }

    private fun initData(item: HistoryDetailResponse.Consultation) {
        binding.apply {
            consultationId = item.id.orEmpty
            whatsapp = item.phone.orEmpty.removePrefix("0")
            vetName = item.vetName.orEmpty
            doctorName = item.doctor.orEmpty
            price = item.price.asIDCurrency()
            discountedPrice = item.discountedPrice.asIDCurrency()
            discountedPrices = item.discountedPrice.orEmpty
            date = formatDateTime.format(
                formatter.parse(item.consultationDate.toString()) ?: Date()
            )
            maxPaymentTime = item.maxPaymentTime.orEmpty

            ivVet.loadImage(item.image, ImageCornerOptions.ROUNDED)
            tvStatus.textOrNull = item.status
            tvVetName.textOrNull = item.vetName
            tvDoctorName.textOrNull = item.doctor
            tvDateConsultation.textOrNull = formatDateTime.format(
                formatter.parse(item.consultationDate.toString()) ?: Date()
            )
            tvPrice.textOrNull = item.price.asIDCurrency()
            if (item.discountedPrice != null && item.discountedPrice != 0) {
                tvDiscountTitle.isVisible = true
                tvDiscount.isVisible = true
                tvDiscount.textOrNull =
                    "-${(item.price.orEmpty - item.discountedPrice.orEmpty).asIDCurrency()}"
                tvTotalPayment.textOrNull = item.discountedPrice.asIDCurrency()
            } else {
                tvDiscountTitle.isVisible = false
                tvDiscount.isVisible = false
                tvTotalPayment.textOrNull = item.price.asIDCurrency()
            }

            when (item.status) {
                context?.getString(R.string.waiting_payment) -> {
                    val maxPaymentDate = formatDate.format(
                        formatter.parse(item.maxPaymentTime.toString()) ?: Date()
                    )
                    tvStatusDesc.textOrNull =
                        context?.getString(R.string.waiting_payment_desc, maxPaymentDate)
                    btnConsultation.textOrNull = context?.getString(R.string.upload_payment)

                    btnConsultation.isVisible = true
                    tvDatePaymentTitle.isVisible = false
                    tvDatePayment.isVisible = false
                    tvDatePaymentConfirm.isVisible = false
                    tvDatePaymentConfirmTitle.isVisible = false
                    tvDatePaymentDeny.isVisible = false
                    tvDatePaymentDenyTitle.isVisible = false
                }
                context?.getString(R.string.waiting_payment_confirm) -> {
                    tvStatusDesc.textOrNull =
                        context?.getString(R.string.waiting_payment_confirm_desc)
                    btnConsultation.textOrNull = context?.getString(R.string.ask_admin)

                    if(item.payAt.orEmpty.isNotEmpty()) {
                        tvDatePayment.textOrNull = formatDateTime.format(
                            formatter.parse(item.payAt.toString()) ?: Date()
                        )
                    }

                    btnConsultation.isVisible = true
                    tvDatePaymentTitle.isVisible = true
                    tvDatePayment.isVisible = true
                    tvDatePaymentConfirm.isVisible = false
                    tvDatePaymentConfirmTitle.isVisible = false
                    tvDatePaymentDeny.isVisible = false
                    tvDatePaymentDenyTitle.isVisible = false
                }
                context?.getString(R.string.payment_deny) -> {
                    tvStatusDesc.textOrNull = context?.getString(R.string.payment_deny_desc)
                    btnConsultation.textOrNull = context?.getString(R.string.upload_payment)

                    if(item.payAt.orEmpty.isNotEmpty()) {
                        tvDatePayment.textOrNull = formatDateTime.format(
                            formatter.parse(item.payAt.toString()) ?: Date()
                        )
                        tvDatePaymentTitle.isVisible = true
                        tvDatePayment.isVisible = true
                    }
                    if(item.rejectedAt.orEmpty.isNotEmpty()) {
                        tvDatePaymentDeny.textOrNull = formatDateTime.format(
                            formatter.parse(item.rejectedAt.toString()) ?: Date()
                        )

                        tvDatePaymentDenyTitle.isVisible = true
                        tvDatePaymentDeny.isVisible = true
                    }

                    btnConsultation.isVisible = true
                    tvDatePaymentConfirm.isVisible = false
                    tvDatePaymentConfirmTitle.isVisible = false
                }
                context?.getString(R.string.consultation_session) -> {
                    tvStatusDesc.textOrNull = context?.getString(R.string.consultation_session_desc)
                    btnConsultation.textOrNull = context?.getString(R.string.start_session)

                    if(item.payAt.orEmpty.isNotEmpty()) {
                        tvDatePayment.textOrNull = formatDateTime.format(
                            formatter.parse(item.payAt.toString()) ?: Date()
                        )
                        tvDatePaymentTitle.isVisible = true
                        tvDatePayment.isVisible = true
                    }
                    if(item.approvedAt.orEmpty.isNotEmpty()) {
                        tvDatePaymentConfirm.textOrNull = formatDateTime.format(
                            formatter.parse(item.approvedAt.toString()) ?: Date()
                        )

                        tvDatePaymentConfirmTitle.isVisible = true
                        tvDatePaymentConfirm.isVisible = true
                    }

                    btnConsultation.isVisible = true
                    tvDatePaymentDeny.isVisible = false
                    tvDatePaymentDenyTitle.isVisible = false
                }
                context?.getString(R.string.done) -> {
                    tvStatusDesc.textOrNull = context?.getString(R.string.done_desc)
                    btnConsultation.textOrNull = context?.getString(R.string.give_review)

                    if(item.payAt.orEmpty.isNotEmpty()) {
                        tvDatePayment.textOrNull = formatDateTime.format(
                            formatter.parse(item.payAt.toString()) ?: Date()
                        )
                        tvDatePaymentTitle.isVisible = true
                        tvDatePayment.isVisible = true
                    }
                    if(item.approvedAt.orEmpty.isNotEmpty()) {
                        tvDatePaymentConfirm.textOrNull = formatDateTime.format(
                            formatter.parse(item.approvedAt.toString()) ?: Date()
                        )
                        tvDatePaymentConfirmTitle.isVisible = true
                        tvDatePaymentConfirm.isVisible = true
                    }

                    btnConsultation.isVisible = true
                    tvDatePaymentDeny.isVisible = false
                    tvDatePaymentDenyTitle.isVisible = false
                }
                context?.getString(R.string.cancelled) -> {
                    tvStatusDesc.textOrNull = context?.getString(R.string.cancelled_desc)
                    btnConsultation.textOrNull = context?.getString(R.string.consultation_again)

                    btnConsultation.isVisible = true
                    tvDatePaymentTitle.isVisible = false
                    tvDatePayment.isVisible = false
                    tvDatePaymentConfirm.isVisible = false
                    tvDatePaymentConfirmTitle.isVisible = false
                    tvDatePaymentDeny.isVisible = false
                    tvDatePaymentDenyTitle.isVisible = false
                }
                else -> {
                    tvStatusDesc.textOrNull = context?.getString(R.string.done_review_desc)

                    btnConsultation.isVisible = false
                    tvDatePaymentTitle.isVisible = false
                    tvDatePayment.isVisible = false
                    tvDatePaymentConfirm.isVisible = false
                    tvDatePaymentConfirmTitle.isVisible = false
                    tvDatePaymentDeny.isVisible = false
                    tvDatePaymentDenyTitle.isVisible = false
                }
            }
        }
    }

    private fun sendWhatsApp(message: String? = null) {
        try {
            val whatsApp = "https://api.whatsapp.com/send?phone=+62$whatsapp&text="
            val packageManager = context?.packageManager
            val whatsappIntent = Intent(Intent.ACTION_VIEW)
            val url = whatsApp + URLEncoder.encode(
                message,
                "UTF-8"
            )
            whatsappIntent.setPackage("com.whatsapp")
            whatsappIntent.data = Uri.parse(url)
            if (packageManager?.let { whatsappIntent.resolveActivity(it) } != null) {
                startActivity(whatsappIntent)
            }
        } catch (e: java.lang.Exception) {
            context?.toast("WhatsApp not Installed")
        }
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "historyDetail")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

    private fun initOnClick() {
        binding.btnConsultation.setOnClickListener(onClickCallback)
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.btnConsultation -> {
                when(binding.btnConsultation.text) {
                    context?.getString(R.string.upload_payment) -> {
                        navController.navigateOrNull(
                            HistoryConsultationDetailFragmentDirections.actionPaymentSummary(
                                consultationId = consultationId,
                                consultationPrice = discountedPrices,
                                paymentDue = maxPaymentTime
                            )
                        )
                    }
                    context?.getString(R.string.ask_admin) -> {
                        whatsapp = "82112872449"
                        sendWhatsApp("Hai, saya customer PawCare, Bagaimana status pembayaran saya?")
                    }
                    context?.getString(R.string.give_review) -> {
                        navController.navigateOrNull(
                            HistoryConsultationDetailFragmentDirections.actionWriteReview(
                                consultationId,
                                vetName,
                                doctorName,
                                price,
                                discountedPrice,
                                date
                            )
                        )
                    }
                    else -> {
                        sendWhatsApp("Halo, saya akan memulai sesi konsultasi")
                    }
                }
            }
        }
    }

}