package com.example.pawcare.data.remote.model


import com.google.gson.annotations.SerializedName

data class HistoryDetailResponse(
    @SerializedName("consultation")
    var consultation: List<Consultation>? = null
) {
    data class Consultation(
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("status")
        var status: String? = null,
        @SerializedName("consultation_date")
        var consultationDate: String? = null,
        @SerializedName("patient")
        var patient: String? = null,
        @SerializedName("doctor")
        var doctor: String? = null,
        @SerializedName("phone")
        var phone: String? = null,
        @SerializedName("image")
        var image: String? = null,
        @SerializedName("vet_name")
        var vetName: String? = null,
        @SerializedName("price")
        var price: Int? = null,
        @SerializedName("discount")
        var discount: Int? = null,
        @SerializedName("discounted_price")
        var discountedPrice: Int? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("pay_at")
        var payAt: String? = null,
        @SerializedName("approved_at")
        var approvedAt: String? = null,
        @SerializedName("rejected_at")
        var rejectedAt: String? = null,
        @SerializedName("max_payment_time")
        var maxPaymentTime: String? = null,
    )
}