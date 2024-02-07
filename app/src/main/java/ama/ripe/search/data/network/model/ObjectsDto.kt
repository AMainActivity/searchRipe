package ama.ripe.search.data.network.model

import com.google.gson.annotations.SerializedName

data class ObjectsDto(

    @SerializedName("object")
    val mObjectList: List<ObjectDto>

)
