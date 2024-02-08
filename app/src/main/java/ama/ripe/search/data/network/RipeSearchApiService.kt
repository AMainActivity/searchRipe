package ama.ripe.search.data.network

import ama.ripe.search.data.network.model.MainDto
import ama.ripe.search.data.network.model.ObjectsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RipeSearchApiService {

    @GET("search.json")
    suspend fun getOrganizationByName(
        @Query("source") source: String = PARAM_RIPE,
        @Query("query-string") queryString: String,
        @Query("type-filter") typeFilter: String = PARAM_ORGANIZATION,
        @Query("flags") flags: String = PARAM_FLAG
    ): Response<MainDto>

    @GET("search.json")
    suspend fun getInetNumByOrg(
        @Query("inverse-attribute") inverseAttribute: String = PARAM_ORG,
        @Query("source") source: String = PARAM_RIPE,
        @Query("query-string") queryString: String,
        @Query("type-filter") typeFilter: String = PARAM_INETNUM,
        // @Query("limit") ofset: Int = 10,
        @Query("flags") flags: String = PARAM_FLAG
    ): Response<MainDto>

    @GET("ripe/organisation/{org}.json")
    suspend fun getOrganizationByIdentificator(
        @Path("org") orgIdentificator: String
    ): Response<MainDto>

    @GET("search.json")
    suspend fun getOrganizationByIp(
        @Query("source") source: String = PARAM_RIPE,
        @Query("query-string") queryString: String,
        @Query("type-filter") typeFilter: String = PARAM_INETNUM,
        // @Query("limit") ofset: Int = 10,
        @Query("flags") flags: String = PARAM_FLAG
    ): Response<MainDto>

    companion object {
        const val PARAM_RIPE = "ripe"
        const val PARAM_INETNUM = "inetnum"
        const val PARAM_FLAG = "no-referenced"
        const val PARAM_ORG = "org"
        const val PARAM_ORGANIZATION = "organisation"
    }
}
