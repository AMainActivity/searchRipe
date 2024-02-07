package ama.ripe.search.data.database

import ama.ripe.search.data.database.models.IpOrganization
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IpDao {
    @Query("SELECT * FROM tab_ip")
    fun getIpList(): List<IpOrganization>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIpList(ipList: List<IpOrganization>): List<Long>
}

