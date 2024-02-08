package ama.ripe.search.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InetNumDomModel(
    val inetNum: String,
    val netName: String,
    val countryFlag: String? = null
) : Parcelable
