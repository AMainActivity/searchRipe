package ama.ripe.search.data.database

import ama.ripe.search.data.database.models.OrganizationDbModel
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OrgObjectDao {
    @Query("SELECT * FROM tab_org")
    fun getOrgObjectList(): List<OrganizationDbModel>

    @Query("SELECT id FROM tab_org where organization=:orgName limit 1")
    fun getIdByOrgName(orgName: String): Int

    @Query("SELECT id FROM tab_org where organization in (:orgName)")
    fun getListIdByOrgName(orgName: List<String>): List<Long>

    @Query("SELECT count(id) as count FROM tab_org")
    fun getCountOrgs(): Int

    @Query("SELECT * FROM tab_org where lower(org_name) like '%' ||:orgName|| '%'")
    fun getOrgListByName(orgName: String): List<OrganizationDbModel>

    @Query("SELECT tab_org.* FROM tab_org,tab_ip where tab_org.id=tab_ip.org_id and lower(tab_ip.ip) like '%' ||:ip|| '%'")
    fun getOrgListByIp(ip: String): List<OrganizationDbModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrgObjectList(priceList: List<OrganizationDbModel>): List<Long>
}

