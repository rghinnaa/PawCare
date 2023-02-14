package com.example.pawcare.ui.consultation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pawcare.data.remote.model.ReviewResponse
import com.example.pawcare.databinding.ItemReviewBinding
import com.example.pawcare.utils.ImageCornerOptions
import com.example.pawcare.utils.loadImage
import com.example.pawcare.utils.orEmpty
import com.example.pawcare.utils.textOrNull
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    private val formatDate = SimpleDateFormat("dd-MM-yyyy", Locale.US)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReviewResponse.Review) {
            binding.run {
                ivUser.loadImage(item.userImage, ImageCornerOptions.ROUNDED)
                tvName.textOrNull = item.userName
                tvReview.textOrNull = item.review
                tvDate.textOrNull = formatDate.format(
                    formatter.parse(item.createdAt.toString()) ?: Date()
                )
                rbReview.rating = item.star.orEmpty.toFloat()
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<ReviewResponse.Review>() {
        override fun areItemsTheSame(
            oldExampleModel: ReviewResponse.Review, newExampleModel: ReviewResponse.Review
        ): Boolean {
            return oldExampleModel.id == newExampleModel.id
        }

        override fun areContentsTheSame(
            oldExampleModel: ReviewResponse.Review, newExampleModel: ReviewResponse.Review
        ): Boolean {
            return oldExampleModel == newExampleModel
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

}