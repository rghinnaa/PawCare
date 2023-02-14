package com.example.pawcare.data.remote.model


import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @SerializedName("consultations")
    var consultations: List<Consultations>? = null
) {
    data class Consultations(
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("doctor")
        var doctor: String? = null,
        @SerializedName("vet_name")
        var vetName: String? = null,
        @SerializedName("price")
        var price: Int? = null,
        @SerializedName("discount")
        var discount: Int? = null,
        @SerializedName("discounted_price")
        var discountedPrice: Int? = null,
        @SerializedName("status")
        var status: String? = null,
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("updated_at")
        var updatedAt: String? = null,
    )
}