package ama.ripe.search.data.database.models

import ama.ripe.search.data.database.models.IpOrganization.Companion.COLUMN_IP
import ama.ripe.search.data.database.models.IpOrganization.Companion.TAB_IP
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = TAB_IP,
    indices = [Index(
        value = [COLUMN_IP],
        unique = true
    )]
)
data class IpOrganization(
    @ColumnInfo(name = COLUMN_ORG_ID)
    val ownerOrgId: Int,
    val ip: String
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = ZERO_LONG

    companion object {
        const val TAB_IP = "tab_ip"
        const val COLUMN_IP = "ip"
        const val COLUMN_ORG_ID = "org_id"
        const val ZERO_LONG = 0L
    }
}
