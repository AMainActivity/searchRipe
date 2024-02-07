package ama.ripe.search.data.database.models

import ama.ripe.search.data.database.models.IpOrganization.Companion.tabIp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = tabIp,
    indices = [Index(
        value = ["ip"],
        unique = true
    )]
)
data class IpOrganization(
    @ColumnInfo(name = "org_id")
    val ownerOrgId: Int,
    val ip: String
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    companion object {
        const val tabIp = "tab_ip"
    }
}
