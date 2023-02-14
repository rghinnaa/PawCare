package com.example.pawcare.ui.profile.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.pawcare.R
import com.example.pawcare.data.remote.Result
import com.example.pawcare.databinding.FragmentHistoryConsultationBinding
import com.example.pawcare.ui.consultation.ConsultationFragmentDirections
import com.example.pawcare.utils.navController
import com.example.pawcare.utils.navigateOrNull
import com.example.pawcare.utils.viewBinding
import com.example.pawcare.viewmodel.MainViewModel
import com.example.pawcare.viewmodel.ProfileViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryConsultationFragment : Fragment(R.layout.fragment_history_consultation) {

    private val binding by viewBinding<FragmentHistoryConsultationBinding>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPagerAdapter()
        initGoToHistoryCallback()
        initFirebaseAnalytics()

    }

    private fun initGoToHistoryCallback() {
        mainViewModel.goToHistory.observe(viewLifecycleOwner) { result ->
            if(mainViewModel.isGoToHistory) {
                binding.vpConsultation.setCurrentItem(result, true)

                mainViewModel.isGoToHistory = false
            }
        }
    }

    private fun setupPagerAdapter() {
        val status = arrayOf(
            "Menunggu Pembayaran",
            "Menunggu Konfirmasi",
            "Ditolak",
            "Sesi Konsultasi",
            "Selesai",
            "Dibatalkan"
        )

        with(binding.vpConsultation) {
            offscreenPageLimit = 1
            adapter = HistoryConsultationPagerAdapter(this@HistoryConsultationFragment).apply {
                setStatus(status)
            }
        }

        with(binding.tlStatus) {
            TabLayoutMediator(this, binding.vpConsultation) { tab, position ->
                tab.text = status[position]
            }.attach()

            fullScroll(View.FOCUS_LEFT)
        }
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "history")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

}