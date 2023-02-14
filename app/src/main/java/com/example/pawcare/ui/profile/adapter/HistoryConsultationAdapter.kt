package com.example.pawcare.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pawcare.data.remote.model.HistoryResponse
import com.example.pawcare.databinding.ItemHistoryConsultationBinding
import com.example.pawcare.utils.asIDCurrency
import com.example.pawcare.utils.dateFormatter
import com.example.pawcare.utils.textOrNull
import java.text.SimpleDateFormat
import java.util.*

class HistoryConsultationAdapter : RecyclerView.Adapter<HistoryConsultationAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryConsultationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position], position = position)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class HistoryViewHolder(private val binding: ItemHistoryConsultationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryResponse.Consultations, position: Int) {

            binding.run {
                tvStatus.textOrNull = item.status
                tvVet.textOrNull = item.vetName
                tvDoctor.textOrNull = item.doctor
                tvDate.textOrNull = item.createdAt.toString()
                tvPrice.textOrNull = item.discountedPrice?.asIDCurrency()

                root.setOnClickListener {
                    onItemClickListener?.invoke(item)
                }
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<HistoryResponse.Consultations>() {
        override fun areItemsTheSame(
            oldExampleModel: HistoryResponse.Consultations, newExampleModel: HistoryResponse.Consultations
        ): Boolean {
            return oldExampleModel.id == newExampleModel.id
        }

        override fun areContentsTheSame(
            oldExampleModel: HistoryResponse.Consultations, newExampleModel: HistoryResponse.Consultations
        ): Boolean {
            return oldExampleModel == newExampleModel
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    private var onItemClickListener: ((HistoryResponse.Consultations) -> Unit)? = null

    fun setOnItemClickListener(listener: (HistoryResponse.Consultations) -> Unit) {
        onItemClickListener = listener
    }

}