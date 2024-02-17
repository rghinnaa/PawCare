package com.example.pawcare.data.remote.model


import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("review")
    var review: List<Review>? = null
) {
    data class Review(
        @SerializedName("_id")
        var id: String? = null,
        @SerializedName("user_name")
        var userName: String? = null,
        @SerializedName("user_image")
        var userImage: String? = null,
        @SerializedName("star")
        var star: Int? = null,
        @SerializedName("review")
        var review: String? = null,
        @SerializedName("created_at")
        var createdAt: String? = null
    )
}