package ama.ripe.search.data.database.models

import ama.ripe.search.data.database.models.OrganizationDbModel.Companion.COLUMN_ORGANIZATION
import ama.ripe.search.data.database.models.OrganizationDbModel.Companion.TAB_ORG
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = TAB_ORG,
    indices = [Index(
        value = [COLUMN_ORGANIZATION],
        unique = true
    )]
)
data class OrganizationDbModel(
    @ColumnInfo(name = COLUMN_ORG_NAME)
    val orgName: String,
    val organization: String,
    @ColumnInfo(name = COLUMN_COUNTRY_FLAG)
    val countryFlag: String? = null,
    val address: String? = null,
    val phone: String? = null,
    @ColumnInfo(name = COLUMN_FAX_NO)
    val faxNo: String? = null
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = ZERO_LONG

    companion object {
        const val TAB_ORG = "tab_org"
        const val COLUMN_ORG_NAME = "org_name"
        const val COLUMN_COUNTRY_FLAG = "country_flag"
        const val COLUMN_FAX_NO = "fax_no"
        const val COLUMN_ORGANIZATION = "organization"
        const val ZERO_LONG = 0L
    }
}
