package com.example.pawcare.ui.notification

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.example.pawcare.R
import com.example.pawcare.utils.toast
import com.google.firebase.analytics.FirebaseAnalytics

class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private var doubleBackToExitOnce = false

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFirebaseAnalytics()
        onBackPressed()

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
        bundle.putString("show_name", "notification")
        mFirebaseAnalytics?.logEvent("show_selected", bundle)
    }

}