package com.example.pawcare.ui.profile.payment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.pawcare.R
import com.example.pawcare.databinding.FragmentPaymentSummaryBinding
import com.example.pawcare.utils.*
import com.example.pawcare.viewmodel.MainViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class PaymentSummaryFragment : Fragment(R.layout.fragment_payment_summary) {

    private val binding by viewBinding<FragmentPaymentSummaryBinding>()
    private val args by navArgs<PaymentSummaryFragmentArgs>()

    private val formatDate = "yyyy-MM-dd HH:mm:ss"

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }

    private fun initView() {
        initOnClick()
        initCountDown(args.paymentDue.orEmpty)
        initFirebaseAnalytics()

        binding.tvBankNumber.textOrNull = Const.BANK_ACCOUNT_NUMBER
        binding.tvTotal.textOrNull = args.consultationPrice.asIDCurrency()
    }

    private fun initCountDown(endDate: String) {
        val prefixFormat = "%02d"
        val date = Date()

        val eventDate = dateFormatter(formatDate)
            .parse(endDate) ?: date

        if (!date.after(eventDate)) {
            viewLifecycleOwner.coroutinesCountDown(repeatMillis = 1000L) {
                val currentDate = Date()
                val eventDateInMillis = eventDate.time - currentDate.time

                val hours = String.format(
                    prefixFormat,
                    (eventDateInMillis.div(60 * 60 * 1000)) % 24
                )
                val minutes = String.format(
                    prefixFormat,
                    (eventDateInMillis.div(60 * 1000) % 60)
                )
                val seconds =
                    String.format(prefixFormat, (eventDateInMillis.div(1000)) % 60)

                val totalHour = hours.toInt()

                binding.apply {
                    if (totalHour.toString().length > 1) tvHour.textOrNull(
                        totalHour.toString(),
                        "0"
                    )
                    else tvHour.textOrNull(
                        getString(
                            R.string.on_time_digit,
                            totalHour.toString()
                        ), "0"
                    )

                    tvMinute.textOrNull(minutes, "0")
                    tvSecond.textOrNull(seconds, "0")

                    if (tvHour.text == "00" && tvMinute.text == "00" && tvSecond.text == "00") {
                        tvHour.textOrNull(
                            getString(
                                R.string.zero_timer
                            )
                        )

                        tvMinute.textOrNull(
                            getString(
                                R.string.zero_timer
                            )
                        )
                        tvSecond.textOrNull(
                            getString(
                                R.string.zero_timer
                            )
                        )
                    }

                }
            }
        }
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "paymentSummaryProfile")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

    private fun initOnClick() {
        binding.apply {
            btnCopyNumber.setOnClickListener(onClickCallback)
            btnCopyTotal.setOnClickListener(onClickCallback)
            btnUploadPayment.setOnClickListener(onClickCallback)
        }
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.btnCopyNumber -> {
                val bankNumber = binding.tvBankNumber.text.toString()

                context?.clip(bankNumber)
                context?.toast(getString(R.string.success_clip, bankNumber))
            }
            binding.btnCopyTotal -> {
                val total = args.consultationPrice

                context?.clip(total.toString())
                context?.toast(getString(R.string.success_clip, total))
            }
            binding.btnUploadPayment -> {
                navController.navigateOrNull(
                    PaymentSummaryFragmentDirections.actionPayment(args.consultationId)
                )
            }
        }
    }

}