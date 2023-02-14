package com.example.pawcare.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.pawcare.R
import com.google.firebase.analytics.FirebaseAnalytics

class EventBannerFragment : Fragment(R.layout.fragment_event_banner) {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFirebaseAnalytics()

    }

    private fun initFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
        val bundle = Bundle()
        bundle.putString("show_name", "eventBanner")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

}