package com.example.pawcare.data.remote.model


import com.google.gson.annotations.SerializedName

data class UserDetailResponse(
    @SerializedName("user_detail")
    var userDetail: List<User>? = null
) {
    data class User(
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("email")
        var email: String? = null,
        @SerializedName("phone")
        var phone: String? = null,
        @SerializedName("address")
        var address: String? = null,
        @SerializedName("gender")
        var gender: String? = null,
        @SerializedName("image")
        var image: String? = null,
        @SerializedName("banner")
        var banner: String? = null
    )
}