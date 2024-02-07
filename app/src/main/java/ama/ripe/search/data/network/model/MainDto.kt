package ama.ripe.search.data.network.model

import com.google.gson.annotations.SerializedName

data class MainDto(
    @SerializedName("objects")
    val mObjects: ObjectsDto
)
