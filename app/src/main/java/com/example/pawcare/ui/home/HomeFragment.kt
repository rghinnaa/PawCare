package com.example.pawcare.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.pawcare.R
import com.example.pawcare.data.remote.Result
import com.example.pawcare.databinding.FragmentHomeBinding
import com.example.pawcare.ui.home.adapter.BannerAdapter
import com.example.pawcare.ui.home.adapter.DoctorAdapter
import com.example.pawcare.ui.main.MainFragment.Companion.parentNavigation
import com.example.pawcare.utils.*
import com.example.pawcare.viewmodel.HomeViewModel
import com.example.pawcare.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding<FragmentHomeBinding>()
    private val viewModel by viewModels<HomeViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    private val bannerAdapter = BannerAdapter()
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
        initOnClick()
        initBannerView()
        initAdapter()
        initAdapterListener()
        initFirebaseAnalytics()
        onBackPressed()
    }

    private fun initViewModel() {
        viewModel.requestBannerList()
        viewModel.requestDoctorList(items = "7")
    }

    private fun initViewModelCallback() {
        initBannerCallback()
        initDoctorCallback()
    }

    private fun initBannerCallback() {
        viewModel.bannerList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    bannerAdapter.differ.submitList(result.results?.bannerList)
                }
                is Result.Error<*> -> {}
                else -> {}
            }
        }
    }

    private fun initDoctorCallback() {
        viewModel.doctorList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {}
                is Result.Success -> {
                    doctorAdapter.differ.submitList(result.results?.doctorList)
                }
                is Result.Error<*> -> {}
                else -> {}
            }
        }
    }

    private fun initAdapter() {
        binding.rvVet.adapter = doctorAdapter
    }

    private fun initAdapterListener() {
        doctorAdapter.setOnItemClickListener { item ->
            mainViewModel.isGoToDetail = true
            mainViewModel.goToDetail(item.id.orEmpty)
            parentNavigation?.selectedItemId = R.id.consultation
        }
    }

    private fun initBannerView() {
        binding.vpBanner.apply {
            adapter = bannerAdapter
            autoScroll(
                lifecycleScope = viewLifecycleOwner.lifecycleScope,
                interval = 4000L
            )
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                var lastPageChange = false
                override fun onPageScrollStateChanged(state: Int) {
                    val lastIdx: Int = childCount - 1

                    val curItem: Int = currentItem
                    if (curItem == lastIdx && state == 1) {
                        lastPageChange = true
                        currentItem = 0
                    } else {
                        lastPageChange = false
                    }
                }
            })
        }

        TabLayoutMediator(binding.tlBanner, binding.vpBanner) { _, _ -> }.attach()

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
        bundle.putString("show_name", "homepage")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

    private fun initOnClick() {
        binding.tvVetSeeAll.setOnClickListener(onClickCallback)
        binding.tEventSeeAll.setOnClickListener(onClickCallback)
    }

    private val onClickCallback = View.OnClickListener { view ->
        when (view) {
            binding.tvVetSeeAll -> {
                parentNavigation?.selectedItemId = R.id.consultation
            }
            binding.tEventSeeAll -> {
                navController.navigateOrNull(
                    HomeFragmentDirections.actionEventBanner()
                )
            }
        }
    }

}