package com.example.pawcare.ui.profile.history

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pawcare.utils.Const
import javax.inject.Inject

class HistoryConsultationPagerAdapter @Inject constructor(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var status: Array<String> = arrayOf()

    fun setStatus(status: Array<String>) {
        this.status = status
    }

    override fun getItemCount(): Int = status.size

    override fun createFragment(position: Int): Fragment =
        HistoryConsultationListFragment().apply {
            arguments = bundleOf(Const.Extras.HISTORY_STATUS to status[position])
        }

}