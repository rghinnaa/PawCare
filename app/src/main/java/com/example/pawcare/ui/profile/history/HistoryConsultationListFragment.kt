package com.example.pawcare.ui.profile.history

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pawcare.R
import com.example.pawcare.data.remote.Result
import com.example.pawcare.data.remote.model.UserDetailResponse
import com.example.pawcare.databinding.FragmentHistoryConsultationListBinding
import com.example.pawcare.ui.consultation.ConsultationFragmentDirections
import com.example.pawcare.ui.profile.adapter.HistoryConsultationAdapter
import com.example.pawcare.utils.*
import com.example.pawcare.viewmodel.ProfileViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryConsultationListFragment : Fragment(R.layout.fragment_history_consultation_list) {

    private val binding by viewBinding<FragmentHistoryConsultationListBinding>()
    private val viewModel by viewModels<ProfileViewModel>()

    private val historyAdapter = HistoryConsultationAdapter()

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initView()
        initViewModel()
        initViewModelCallback()

    }

    private fun initView() {
        initAdapter()
        initAdapterListener()
        initFirebaseAnalytics()
    }

    private fun initViewModel() {
        viewModel.requestHistory(arguments?.getString(Const.Extras.HISTORY_STATUS).toString())
    }

    private fun initViewModelCallback() {
        initHistoryCallback()
    }

    private fun initHistoryCallback() {
        viewModel.history.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    if(result.results?.consultations.orEmpty().isNotEmpty()) {
                        binding.clEmpty.isVisible = false
                        historyAdapter.differ.submitList(result.results?.consultations)
                    } else {
                        binding.clEmpty.isVisible = true
                    }
                }
                is Result.Error<*> -> {}
                else -> {}
            }
        }
    }

    private fun initAdapter() {
        binding.rvHistory.adapter = historyAdapter
    }

    private fun initAdapterListener() {
        historyAdapter.setOnItemClickListener { item ->
            navController.navigateOrNull(
                HistoryConsultationFragmentDirections.actionHistoryConsultationDetail(item.id.orEmpty)
            )
        }
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "historyList")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

}