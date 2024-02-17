package com.example.pawcare.data.remote.model


import com.google.gson.annotations.SerializedName

data class DoctorDetailResponse(
    @SerializedName("doctor_list")
    var doctorList: List<Doctor>? = null
) {
    data class Doctor(
        @SerializedName("_id")
        var id: String? = null,
        @SerializedName("user_doctor_detail_id")
        var userDoctorDetailId: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("address")
        var address: String? = null,
        @SerializedName("vet_name")
        var vetName: String? = null,
        @SerializedName("image")
        var image: String? = null,
        @SerializedName("banner")
        var banner: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("price")
        var price: Int? = null,
        @SerializedName("discount")
        var discount: Int? = null,
        @SerializedName("discounted_price")
        var discountedPrice: Double? = null,
        @SerializedName("avg_ratings")
        var avgRatings: String? = null,
        @SerializedName("consultation_count")
        var consultationCount: Int? = null
    )
}