package ama.ripe.search.data.database

import ama.ripe.search.data.database.models.InetNumDbModel
import ama.ripe.search.data.database.models.OrganizationDbModel
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface InetNumObjectDao {
    @Query("SELECT * FROM tab_inet_num")
    fun getInetNumObjectList(): List<InetNumDbModel>

    @Query(
        "SELECT tab_inet_num.* FROM tab_inet_num, tab_org " +
                "where tab_org.id=tab_inet_num.org_id" +
                " and tab_inet_num.org_id=:orgId"
    )
    fun getInetNumObjectListByOrg(orgId: Int): List<InetNumDbModel>


    @Query("SELECT tab_inet_num.* FROM tab_org,tab_inet_num where tab_org.id=tab_inet_num.org_id and tab_org.organization= :orgName")
    fun getInetNumListByOrgName(orgName: String): List<InetNumDbModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInetNumObjectList(priceList: List<InetNumDbModel>): List<Long>
}

