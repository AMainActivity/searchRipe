package ama.ripe.search.data.database.models

import ama.ripe.search.data.database.models.OrganizationDbModel.Companion.tabOrg
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = tabOrg,
    indices = [Index(
        value = ["organization"],
        unique = true
    )]
)
data class OrganizationDbModel(
    @ColumnInfo(name = "org_name")
    val orgName: String,
    val organization: String,
    @ColumnInfo(name = "country_flag")
    val countryFlag: String? = null,
    val address: String? = null,
    val phone: String? = null,
    @ColumnInfo(name = "fax_no")
    val faxNo: String? = null
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    companion object {
        const val tabOrg = "tab_org"
    }
}
