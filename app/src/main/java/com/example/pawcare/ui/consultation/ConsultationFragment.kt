package com.example.pawcare.ui.consultation

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.pawcare.R
import com.example.pawcare.data.remote.Result
import com.example.pawcare.databinding.FragmentConsultationBinding
import com.example.pawcare.ui.consultation.adapter.DoctorAdapter
import com.example.pawcare.utils.*
import com.example.pawcare.viewmodel.ConsultationViewModel
import com.example.pawcare.viewmodel.MainViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConsultationFragment : Fragment(R.layout.fragment_consultation) {

    private val binding by viewBinding<FragmentConsultationBinding>()
    private val viewModel by viewModels<ConsultationViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    private val doctorAdapter = DoctorAdapter()
    private var doubleBackToExitOnce = false

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        initViewModelCallback()

    }

    private fun initView() {
        initSearch()
        initAdapter()
        initAdapterListener()
        initFirebaseAnalytics()
        onBackPressed()
    }

    private fun initViewModel() {
        viewModel.requestDoctorList(
            binding.etSearch.text.toString().trim(),
            "100"
        )
    }

    private fun initViewModelCallback() {
        initDoctorCallback()
        initGoToDetailCallback()
    }

    private fun initDoctorCallback() {
        viewModel.doctorList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    if(result.results?.doctorList.orEmpty().isNotEmpty()) {
                        binding.clEmpty.isVisible = false
                        doctorAdapter.differ.submitList(result.results?.doctorList)
                    } else {
                        binding.clEmpty.isVisible = true
                    }
                }
                is Result.Error<*> -> {}
                else -> {}
            }
        }
    }

    private fun initGoToDetailCallback() {
        mainViewModel.goToDetail.observe(viewLifecycleOwner) { result ->
            if(mainViewModel.isGoToDetail) {
                navController.navigateOrNull(
                    ConsultationFragmentDirections.actionConsultationDetail(result)
                )

                mainViewModel.isGoToDetail = false
            }
        }
    }

    private fun initAdapter() {
        binding.rvVet.adapter = doctorAdapter
    }

    private fun initAdapterListener() {
        doctorAdapter.setOnItemClickListener { item ->
            navController.navigateOrNull(
                ConsultationFragmentDirections.actionConsultationDetail(item.id.orEmpty)
            )
        }
    }

    private fun initSearch() {
        binding.etSearch.addDelayOnTypeWithScope(50, lifecycleScope) {
            viewModel.requestDoctorList(
                binding.etSearch.text.toString().trim(),
                "100"
            )
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
        bundle.putString("show_name", "consultation")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

}