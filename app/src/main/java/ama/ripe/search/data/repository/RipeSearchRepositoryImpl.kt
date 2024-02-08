package ama.ripe.search.data.repository

import ama.ripe.search.R
import ama.ripe.search.data.database.InetNumObjectDao
import ama.ripe.search.data.database.IpDao
import ama.ripe.search.data.database.OrgObjectDao
import ama.ripe.search.data.database.models.IpOrganization
import ama.ripe.search.data.database.models.OrganizationDbModel
import ama.ripe.search.data.mapper.dtoToInetNUMEntities
import ama.ripe.search.data.mapper.toDbs
import ama.ripe.search.data.mapper.toDomEntities
import ama.ripe.search.data.mapper.toEntities
import ama.ripe.search.data.mapper.toInetNumDbs
import ama.ripe.search.data.mapper.toInetDomEntities
import ama.ripe.search.data.network.RipeSearchApiService
import ama.ripe.search.data.network.model.ObjectDto
import ama.ripe.search.domain.entity.InetNumDomModel
import ama.ripe.search.domain.entity.OrganizationDomModel
import ama.ripe.search.domain.repository.RipeSearchRepository
import ama.ripe.search.presentation.StateLoading
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.InetAddresses
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject


class RipeSearchRepositoryImpl @Inject constructor(
    private val apiService: RipeSearchApiService,
    private val orgObjectDao: OrgObjectDao,
    private val inetNumObjectDao: InetNumObjectDao,
    private val ipDao: IpDao,
    private val application: Application
) : RipeSearchRepository {

    private val emptyBody = application.getString(R.string.empty_body)
    private val emptyList = application.getString(R.string.empty_list)

    private val _organizationState =
        MutableLiveData<StateLoading<OrganizationDomModel>>(StateLoading.Initial)
    private val organizationState: LiveData<StateLoading<OrganizationDomModel>> = _organizationState


    private val _inetNumState = MutableLiveData<StateLoading<InetNumDomModel>>(StateLoading.Initial)
    private val inetNumState: LiveData<StateLoading<InetNumDomModel>> = _inetNumState

    private fun checkInternet(): Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                )
            }
            return false
        } else {
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }

    private suspend fun getOrgInfo(string: String) {
        val currentState = _organizationState.value
        if (currentState !is StateLoading.Content || currentState.currencyList != null) {
            _organizationState.postValue(StateLoading.Loading)
        }

        val mResponse = apiService.getOrganizationByName(queryString = string)
        val mBody = mResponse.body()?.mObjects?.mObjectList
        if (mResponse.isSuccessful) {
            if (mBody != null) {
                orgObjectDao.insertOrgObjectList(mBody.toDbs())
                if (mBody.toEntities().isEmpty())
                    _organizationState.postValue(StateLoading.ContentError(emptyList))
                else
                    _organizationState.postValue(StateLoading.Content(mBody.toEntities()))
            } else {
                _organizationState.postValue(StateLoading.ContentError(emptyBody))
            }
        } else {
            _organizationState.postValue(
                StateLoading.ContentError(
                    getErrorCode(mResponse.code(), string)
                )
            )
        }
    }

    override suspend fun getOrgList() {
        val res = orgObjectDao.getOrgObjectList()
        if (res.isEmpty())
            _organizationState.postValue(StateLoading.ContentError(emptyList))
        else
            _organizationState.postValue(StateLoading.Content(res.toDomEntities()))
    }

    private fun getOrgListByName(name: String) {
        val res = orgObjectDao.getOrgListByName(name)
        if (res.isEmpty())
            _organizationState.postValue(StateLoading.ContentError(emptyList))
        else
            _organizationState.postValue(StateLoading.Content(res.toDomEntities()))
    }

    private fun getOrgListByIp(ip: String) {
        val res = orgObjectDao.getOrgListByIp(ip)
        if (res.isEmpty())
            _organizationState.postValue(StateLoading.ContentError(emptyList))
        else
            _organizationState.postValue(StateLoading.Content(res.toDomEntities()))
    }

    override suspend fun getOrganization(string: String) {

        if (checkInternet()) {
            if (isIpValid(string)) getInetNumByIp(string) else getOrgInfo(string)
        } else {
            if (isIpValid(string)) getOrgListByIp(string) else getOrgListByName(string)
        }
    }

    override fun getOrganizationLivaData() = organizationState

    override fun getInetNumLivaData() = inetNumState

    private fun isIpValid(ip: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            InetAddresses.isNumericAddress(ip)
        } else {
            Patterns.IP_ADDRESS.matcher(ip).matches()
        }
    }

    private suspend fun insertIps(ipString: String, list: List<OrganizationDbModel>) {
        val listLong = orgObjectDao.insertOrgObjectList(list)
        val listLong2 = orgObjectDao.getListIdByOrgName(list.map { it.organization })
        ipDao.insertIpList((listLong.plus(listLong2))
            .filter { it >= 0 }
            .map {
                IpOrganization(
                    ownerOrgId = it.toInt(),
                    ip = ipString
                )
            })
    }

    private suspend fun getOrgNameByIdentificator(identificator: String): List<ObjectDto> {
        val mResponse = apiService.getOrganizationByIdentificator(identificator)
        val mBody = mResponse.body()?.mObjects?.mObjectList?.get(FIRST_INDEX)
        return if (mResponse.isSuccessful) {
            if (mBody != null) {
                listOf(mBody)
            } else {
                listOf()
            }
        } else {
            listOf()
        }
    }

    private suspend fun getInetNumByIp(ipString: String) {

        val currentState = _organizationState.value
        if (currentState !is StateLoading.Content || currentState.currencyList != null) {
            _organizationState.postValue(StateLoading.Loading)
        }
        val mResponse = apiService.getOrganizationByIp(queryString = ipString)
        val mBody = mResponse.body()?.mObjects?.mObjectList
        if (mResponse.isSuccessful) {
            if (mBody != null) {
                val temp =
                    mBody[FIRST_INDEX].mAttributes.mAttribute.filter { it.mName.contains(PARAM_ORG) }
                val list = if (temp.isNotEmpty()) {
                    getOrgNameByIdentificator(temp[FIRST_INDEX].mValue)
                } else listOf()

                if (list.isNotEmpty()) {
                    insertIps(ipString, list.toDbs())
                    _organizationState.postValue(StateLoading.Content(list.toEntities()))
                } else {
                    _organizationState.postValue(StateLoading.ContentError(emptyList))
                }
            } else {
                _organizationState.postValue(StateLoading.ContentError(emptyBody))
            }
        } else {
            _organizationState.postValue(
                StateLoading.ContentError(
                    getErrorCode(mResponse.code(), ipString)
                )
            )
        }
    }

    override suspend fun getInetNumByOrg(orgName: String) {
        if (checkInternet()) {
            getInetNumByOrgFromNet(orgName)
        } else {
            getInetNumByOrgFromDb(orgName)
        }

    }

    private fun getInetNumByOrgFromDb(orgName: String) {
        val res = inetNumObjectDao.getInetNumListByOrgName(orgName)
        if (res.isEmpty())
            _inetNumState.postValue(StateLoading.ContentError(emptyList))
        else
            _inetNumState.postValue(StateLoading.Content(res.toInetDomEntities()))

    }


    override fun getCountOfOrg() = orgObjectDao.getCountOrgs()

    private fun getOrgIdByName(orgName: String) = orgObjectDao.getIdByOrgName(orgName)

    private suspend fun insertInetNum(id: Int, list: List<ObjectDto>) {
        inetNumObjectDao.insertInetNumObjectList(
            list.toInetNumDbs(id)
        )
    }

    private suspend fun getInetNumByOrgFromNet(orgName: String) {
        val currentState = _inetNumState.value
        if (currentState !is StateLoading.Content || currentState.currencyList != null) {
            _inetNumState.postValue(StateLoading.Loading)
        }
        val mResponse = apiService.getInetNumByOrg(queryString = orgName)
        val mBody = mResponse.body()?.mObjects?.mObjectList
        val orgId = getOrgIdByName(orgName)
        if (mResponse.isSuccessful) {
            if (mBody != null) {
                insertInetNum(orgId, mBody)
                if (mBody.toEntities().isEmpty())
                    _inetNumState.postValue(StateLoading.ContentError(emptyList))
                else
                    _inetNumState.postValue(StateLoading.Content(mBody.dtoToInetNUMEntities()))
            } else {
                _inetNumState.postValue(StateLoading.ContentError(emptyBody))
            }
        } else {
            _inetNumState.postValue(
                StateLoading.ContentError(
                    getErrorCode(mResponse.code(), orgName)
                )
            )
        }
    }

    private fun getErrorCode(code: Int, string: String?) = when (code) {
        CODE_404 -> String.format(CODE_404_TEXT, string ?: EMPTY_STRING)
        CODE_400 -> CODE_400_TEXT
        CODE_200 -> CODE_200_TEXT
        else -> String.format(CODE_ELSE_TEXT, code)
    }


    companion object {
        const val FIRST_INDEX = 0
        const val PARAM_ORG = "org"
        const val EMPTY_STRING = ""
        const val CODE_404 = 404
        const val CODE_400 = 400
        const val CODE_200 = 200
        const val CODE_404_TEXT = "No object(s) found: %s"
        const val CODE_400_TEXT = "Illegal input - incorrect value in one or more of the parameters"
        const val CODE_200_TEXT = "Search successful"
        const val CODE_ELSE_TEXT = "Error %s"
    }

}
