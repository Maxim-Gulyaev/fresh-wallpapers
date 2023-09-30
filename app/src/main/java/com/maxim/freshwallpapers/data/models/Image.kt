package com.maxim.freshwallpapers.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    @SerializedName("id")
    val id: String,
    @SerializedName("largeImageURL")
    val largeImageURL: String,
    @SerializedName("webformatURL")
    val previewURL: String,
    @SerializedName("user")
    val user: String,
    @SerializedName("pageURL")
    val pageURL: String
): Parcelable
