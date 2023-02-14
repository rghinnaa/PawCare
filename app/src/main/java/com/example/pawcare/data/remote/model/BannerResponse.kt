package com.example.pawcare.data.remote.model


import com.google.gson.annotations.SerializedName

data class BannerResponse(
    @SerializedName("banner_list")
    var bannerList: List<Banner>? = null
) {
    data class Banner(
        @SerializedName("sequence")
        var sequence: Int? = null,
        @SerializedName("image")
        var image: String? = null
    )
}