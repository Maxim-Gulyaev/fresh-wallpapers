package android.maxim.freshwallpapers.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val id: String,
    val largeImageURL: String,
    val previewURL: String
): Parcelable
