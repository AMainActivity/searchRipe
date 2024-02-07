package ama.ripe.search.data.network.model

import com.google.gson.annotations.SerializedName

data class AttributesDto(
    @SerializedName("attribute")
    val mAttribute: List<AttributeDto>
)
