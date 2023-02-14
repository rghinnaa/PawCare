package com.example.pawcare.data.remote.model


import com.google.gson.annotations.SerializedName

data class ConsultationResponse(
    @SerializedName("consultation")
    var consultation: List<Consultation>? = null
) {
    data class Consultation(
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("status")
        var status: String? = null,
        @SerializedName("consultation_date")
        var consultationDate: String? = null,
        @SerializedName("patient")
        var patient: String? = null,
        @SerializedName("doctor")
        var doctor: String? = null,
        @SerializedName("vet_name")
        var vetName: String? = null,
        @SerializedName("price")
        var price: Int? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("max_payment_time")
        var maxPaymentTime: String? = null,
    )
}