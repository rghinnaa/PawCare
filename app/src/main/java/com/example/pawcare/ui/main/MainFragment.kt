package com.example.pawcare.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.pawcare.R
import com.example.pawcare.databinding.FragmentMainBinding
import com.example.pawcare.ui.main.MainActivity.Companion.reInflateMainNavGraph
import com.example.pawcare.utils.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics

class MainFragment : Fragment(R.layout.fragment_main) {

    private val bottomNavSelectedItemIdKey = "BOTTOM_NAV_SELECTED_ITEM_ID_KEY"
    private var bottomNavSelectedItemId = R.id.home

    private val binding by viewBinding<FragmentMainBinding>()

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private val resToolbarId = mutableListOf(
        R.id.home_fragment,
        R.id.consultation_fragment
    )
    private val resNavigationId = mutableListOf(
        R.id.consultation_detail_fragment,
        R.id.payment_summary_fragment,
        R.id.payment_fragment,
        R.id.dialog_add_photo,
        R.id.history_consultation_fragment,
        R.id.history_consultation_detail_fragment,
        R.id.write_review_fragment,
        R.id.profile_edit_fragment,
        R.id.change_password_fragment,
        R.id.event_banner_fragment
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentToolbar?.apply {
            setTitleTextColor(context.color(R.color.black))
            setTitleTextAppearance(context, R.style.TextAppearance_Roboto_Black_Body2)
            elevation = context.dimen(R.dimen.medium_divider)
        }

        savedInstanceState?.let {
            bottomNavSelectedItemId = it.getInt(bottomNavSelectedItemIdKey, bottomNavSelectedItemId)
        }

        setUpBottomNavBar()
        initFirebaseAnalytics()

    }

    private fun setUpBottomNavBar() {
        binding.bottomNavigation.selectedItemId = bottomNavSelectedItemId
        binding.bottomNavigation.itemIconTintList = null

        val navGraphIds = listOf(
            R.navigation.nav_main,
            R.navigation.nav_consultation,
            R.navigation.nav_notification,
            R.navigation.nav_profile
        )

        val controller = binding.bottomNavigation.setupWithNavController(
            fragmentManager = childFragmentManager,
            navGraphIds = navGraphIds,
            backButtonBehaviour = BackButtonBehaviour.POP_HOST_FRAGMENT,
            containerId = binding.navHostContainer.id,
            firstItemId = R.id.home,
            intent = requireActivity().intent
        )

        controller.observe(viewLifecycleOwner) { navController ->
            NavigationUI.setupWithNavController(binding.mainToolbar, navController)
            bottomNavSelectedItemId = navController.graph.id

            navController.addOnDestinationChangedListener { _, destination, _ ->
                binding.mainToolbar.isVisible = !resToolbarId.any { resId ->
                    destination.id == resId
                }

                binding.bottomNavigation.isVisible = !resNavigationId.any { resId ->
                    destination.id == resId
                }

                if (destination.id == R.id.main_fragment) reInflateMainNavGraph()
            }
        }
    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "main")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

    companion object {
        val Fragment?.parentToolbar: MaterialToolbar?
            get() {
                val fragment = if (this?.parentFragment is NavHostFragment) {
                    (parentFragment as? NavHostFragment)?.parentFragment as? MainFragment
                } else null

                return try {
                    fragment?.binding?.mainToolbar
                } catch (e: Exception) {
                    null
                }
            }

        val Fragment?.parentNavigation: BottomNavigationView?
            get() {
                val fragment = if (this?.parentFragment is NavHostFragment) {
                    (parentFragment as? NavHostFragment)?.parentFragment as? MainFragment
                } else null

                return try {
                    fragment?.binding?.bottomNavigation
                } catch (e: Exception) {
                    null
                }
            }
    }

}