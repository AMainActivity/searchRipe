package ama.ripe.search.data.repository

import ama.ripe.search.data.database.InetNumObjectDao
import ama.ripe.search.data.database.IpDao
import ama.ripe.search.data.database.OrgObjectDao
import ama.ripe.search.data.database.models.IpOrganization
import ama.ripe.search.data.database.models.OrganizationDbModel
import ama.ripe.search.data.mapper.toDbs
import ama.ripe.search.data.mapper.toDomEntities
import ama.ripe.search.data.mapper.toEntities
import ama.ripe.search.data.mapper.toInetNumDbs
import ama.ripe.search.data.mapper.toInetNumEntities
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
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import javax.inject.Inject


class RipeSearchRepositoryImpl @Inject constructor(
    private val apiService: RipeSearchApiService,
    private val orgObjectDao: OrgObjectDao,
    private val inetNumObjectDao: InetNumObjectDao,
    private val ipDao: IpDao,
    private val application: Application
) : RipeSearchRepository {


    private val _organizationState =
        MutableLiveData<StateLoading<OrganizationDomModel>>(StateLoading.Initial)
    val organizationState: LiveData<StateLoading<OrganizationDomModel>> = _organizationState


    private val _inetNumState = MutableLiveData<StateLoading<InetNumDomModel>>(StateLoading.Initial)
    val inetNumState: LiveData<StateLoading<InetNumDomModel>> = _inetNumState

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
                    _organizationState.postValue(StateLoading.ContentError("Empty list"))
                else
                    _organizationState.postValue(StateLoading.Content(mBody.toEntities()))
            } else {
                _organizationState.postValue(StateLoading.ContentError("Empty body"))
            }
        } else {

            Log.e("ContentError", "ContentError")
            _organizationState.postValue(
                StateLoading.ContentError(
                    getErrorCode(mResponse.code(), string)
                )
            )

        }

    }

    //при старте приложения
    override suspend fun getOrgList() {
        val res = orgObjectDao.getOrgObjectList()
        if (res.isEmpty())
            _organizationState.postValue(StateLoading.ContentError("Empty list"))
        else
            _organizationState.postValue(StateLoading.Content(res.toDomEntities()))
    }

    private fun getOrgListByName(name: String) {
        val res = orgObjectDao.getOrgListByName(name)
        if (res.isEmpty())
            _organizationState.postValue(StateLoading.ContentError("Empty list"))
        else
            _organizationState.postValue(StateLoading.Content(res.toDomEntities()))
    }


    private fun getOrgListByIp(ip: String) {
        val res = orgObjectDao.getOrgListByIp(ip)
        if (res.isEmpty())
            _organizationState.postValue(StateLoading.ContentError("Empty list"))
        else
            _organizationState.postValue(StateLoading.Content(res.toDomEntities()))
    }

    override suspend fun getOrganization(string: String)/*: List<OrganizationDomModel>*/ {

        if (checkInternet()) {
            Log.e("checkInternet", "true")
            if (isIpValid(string)) getInetNumByIp(string) else getOrgInfo(string)
        } else {
            Log.e("checkInternet", "false")
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
        var listLong = orgObjectDao.insertOrgObjectList(list)
        var listLong2 = orgObjectDao.getListIdByOrgName(list.map { it.organization })
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
        Log.e("identificator", identificator)
        val mResponse = apiService.getOrganizationByIdentificator(identificator)
        val mBody = mResponse.body()?.mObjects?.mObjectList?.get(0)
        Log.e("identificator5", mResponse.body().toString())
        return if (mResponse.isSuccessful) {
            Log.e("identificator4", mBody.toString())
            if (mBody != null) {
                listOf(mBody)
            } else {
                listOf()
            }
        } else {
            listOf()
        }
    }

    //ip
    private suspend fun getInetNumByIp(ipString: String) {

        val currentState = _organizationState.value
        if (currentState !is StateLoading.Content || currentState.currencyList != null) {
            _organizationState.postValue(StateLoading.Loading)
        }

        val mResponse = apiService.getOrganizationByIp(queryString = ipString)
        val mBody = mResponse.body()?.mObjects?.mObjectList
        if (mResponse.isSuccessful) {
            if (mBody != null) {
                Log.e("identificator1", mBody.toString())
                val identificator =
                    mBody[0].mAttributes.mAttribute.filter { it.mName.contains("org") }[0].mValue
                val list = getOrgNameByIdentificator(identificator)
                Log.e("identificator2", list.toString())
                /*
                val list=listOf(Attr("org","111"),Attr("adres","www"),Attr("pnone","02"))
        val s=list.filter{it.name.contains("org")}
        println(s[0].value.toString())
                */
                if (list.isNotEmpty()) {
                    insertIps(ipString, list.toDbs())
                    /*if (list.toEntities().isEmpty())
                        _organizationState.postValue(StateLoading.ContentError("Empty list"))
                    else*/
                    _organizationState.postValue(StateLoading.Content(list.toEntities()))
                } else {
                    _organizationState.postValue(StateLoading.ContentError("Empty body"))
                }
            } else {
                _organizationState.postValue(StateLoading.ContentError("Empty body"))
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
            Log.e("checkInternet", "true")
        } else {
            getInetNumByOrgFromDb(orgName)
            Log.e("checkInternet", "false")
        }

    }

    private fun getInetNumByOrgFromDb(orgName: String) {

        val res = inetNumObjectDao.getInetNumListByOrgName(orgName)
        if (res.isEmpty())
            _inetNumState.postValue(StateLoading.ContentError("Empty list"))
        else
            _inetNumState.postValue(StateLoading.Content(res.toInetNumEntities()))

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
                    _inetNumState.postValue(StateLoading.ContentError("Empty list"))
                else
                    _inetNumState.postValue(StateLoading.Content(mBody.toInetNumEntities(orgId)))
            } else {
                _inetNumState.postValue(StateLoading.ContentError("Empty body"))
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
        404 -> "No object(s) found: ${string ?: ""}"
        400 -> "Illegal input - incorrect value in one or more of the parameters"
        200 -> "Search successful"
        else -> "Error"
    }
}
