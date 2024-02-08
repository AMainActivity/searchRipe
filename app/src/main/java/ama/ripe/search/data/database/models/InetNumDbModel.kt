package ama.ripe.search.data.database.models

import ama.ripe.search.data.database.models.InetNumDbModel.Companion.COLUMN_INET_NUM
import ama.ripe.search.data.database.models.InetNumDbModel.Companion.TAB_INET_NUM
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = TAB_INET_NUM,
    indices = [Index(
        value = [COLUMN_INET_NUM],
        unique = true
    )]
)
data class InetNumDbModel(
    @ColumnInfo(name = COLUMN_ORG_ID)
    val ownerOrgId: Int,
    @ColumnInfo(name = COLUMN_INET_NUM)
    val inetNum: String,
    @ColumnInfo(name = COLUMN_NET_NAME)
    val netName: String,
    @ColumnInfo(name = COLUMN_COUNTRY_FLAG)
    val countryFlag: String? = null
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = ZERO_LONG

    companion object {
        const val TAB_INET_NUM = "tab_inet_num"
        const val COLUMN_INET_NUM = "inet_num"
        const val COLUMN_NET_NAME = "net_name"
        const val COLUMN_ORG_ID = "org_id"
        const val COLUMN_COUNTRY_FLAG = "country_flag"
        const val ZERO_LONG = 0L
    }
}
