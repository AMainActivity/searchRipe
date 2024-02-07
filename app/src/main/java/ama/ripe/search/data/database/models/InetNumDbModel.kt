package ama.ripe.search.data.database.models

import ama.ripe.search.data.database.models.InetNumDbModel.Companion.tabInetNum
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = tabInetNum,
    indices = [Index(
        value = ["inet_num"],
        unique = true
    )]
)
data class InetNumDbModel(
    @ColumnInfo(name = "org_id")
    val ownerOrgId: Int,
    @ColumnInfo(name = "inet_num")
    val inetNum: String,
    @ColumnInfo(name = "net_name")
    val netName: String,
    @ColumnInfo(name = "country_flag")
    val countryFlag: String? = null
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    companion object {
        const val tabInetNum = "tab_inet_num"
        const val fieldOrgId = "id"
        const val fieldOwnerId = "org_id"
    }
}