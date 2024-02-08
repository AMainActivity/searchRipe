package ama.ripe.search.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrganizationDomModel(
    val orgName: String,
    val organization: String,
    val countryFlag: String? = null,
    val address: String,
    val phone: String,
    val faxNo: String
) : Parcelable
