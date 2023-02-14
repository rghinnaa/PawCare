package com.example.pawcare.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pawcare.data.remote.model.DoctorResponse
import com.example.pawcare.databinding.ItemVetHomeBinding
import com.example.pawcare.utils.ImageCornerOptions
import com.example.pawcare.utils.loadImage
import com.example.pawcare.utils.textOrNull

class DoctorAdapter : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ItemVetHomeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        holder.bind(item = differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class DoctorViewHolder(private val binding: ItemVetHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DoctorResponse.Doctor) {
            binding.run {
                ivVet.loadImage(source = item.image, corner = ImageCornerOptions.ROUNDED)
                tvVet.textOrNull = item.vetName
                tvDoctor.textOrNull = item.name
                tvRating.textOrNull = item.avgRatings
                tvPatient.textOrNull = item.consultationCount.toString()

                root.setOnClickListener {
                    onItemClickListener?.invoke(item)
                }
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<DoctorResponse.Doctor>() {
        override fun areItemsTheSame(
            oldExampleModel: DoctorResponse.Doctor, newExampleModel: DoctorResponse.Doctor
        ): Boolean {
            return oldExampleModel.id == newExampleModel.id
        }

        override fun areContentsTheSame(
            oldExampleModel: DoctorResponse.Doctor, newExampleModel: DoctorResponse.Doctor
        ): Boolean {
            return oldExampleModel == newExampleModel
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    private var onItemClickListener: ((DoctorResponse.Doctor) -> Unit)? = null

    fun setOnItemClickListener(listener: (DoctorResponse.Doctor) -> Unit) {
        onItemClickListener = listener
    }

}