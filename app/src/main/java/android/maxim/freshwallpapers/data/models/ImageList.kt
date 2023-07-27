package android.maxim.freshwallpapers.data.models

import com.google.gson.annotations.SerializedName

data class ImageList(
    @SerializedName("hits")
    val imageList: List<Image>
)

// TODO do not like this class, consider to rid it