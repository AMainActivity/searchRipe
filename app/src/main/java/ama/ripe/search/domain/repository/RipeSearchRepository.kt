package ama.ripe.search.domain.repository

import ama.ripe.search.domain.entity.InetNumDomModel
import ama.ripe.search.domain.entity.OrganizationDomModel
import ama.ripe.search.presentation.StateLoading
import androidx.lifecycle.LiveData

interface RipeSearchRepository {
    suspend fun getOrgList()
    suspend fun getOrganization(string: String)
    fun getOrganizationLivaData(): LiveData<StateLoading<OrganizationDomModel>>
    fun getInetNumLivaData(): LiveData<StateLoading<InetNumDomModel>>
    suspend fun getInetNumByOrg(string: String)
    fun getCountOfOrg(): Int
}