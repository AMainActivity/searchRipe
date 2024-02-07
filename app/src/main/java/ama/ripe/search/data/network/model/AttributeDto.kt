package ama.ripe.search.data.network.model

import com.google.gson.annotations.SerializedName

data class AttributeDto(
    @SerializedName("name")
    val mName: String,
    @SerializedName("value")
    val mValue: String

)
