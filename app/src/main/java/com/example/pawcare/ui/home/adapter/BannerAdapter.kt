package com.example.pawcare.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pawcare.data.remote.model.BannerResponse
import com.example.pawcare.databinding.ItemBannerBinding
import com.example.pawcare.utils.loadImage

class BannerAdapter : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position], position = position)

    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class BannerViewHolder(private val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(item: BannerResponse.Banner, position: Int) {
                binding.run {
                    ivBanner.loadImage(item.image)
                }
            }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<BannerResponse.Banner>() {
        override fun areItemsTheSame(
            oldExampleModel: BannerResponse.Banner, newExampleModel: BannerResponse.Banner
        ): Boolean {
            return oldExampleModel.sequence == newExampleModel.sequence
        }

        override fun areContentsTheSame(
            oldExampleModel: BannerResponse.Banner, newExampleModel: BannerResponse.Banner
        ): Boolean {
            return oldExampleModel == newExampleModel
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

}