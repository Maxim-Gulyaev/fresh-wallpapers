package android.maxim.freshwallpapers.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LikedImageMap(
    val likedImageMap: MutableMap<String,Image>
): Parcelable
