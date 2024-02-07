package ama.ripe.search.data.network.model

import com.google.gson.annotations.SerializedName

data class ObjectDto(
    @SerializedName("attributes")
    val mAttributes: AttributesDto,
    @SerializedName("primary-key")
    val mPrimaryKey: PrimaryKeyDto
)

data class PrimaryKeyDto(

    @SerializedName("attribute") val mAttribute: List<AttributeDto>

)
