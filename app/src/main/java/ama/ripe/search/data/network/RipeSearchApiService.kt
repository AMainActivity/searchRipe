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
        @Query("source") source: String = "ripe",
        @Query("query-string") queryString: String,
        @Query("type-filter") typeFilter: String = "organisation",
        @Query("flags") flags: String = "no-referenced"
    ): Response<MainDto>


    @GET("search.json")
    suspend fun getInetNumByOrg(
        @Query("inverse-attribute") inverseAttribute: String = "org",
        @Query("source") source: String = "ripe",
        @Query("query-string") queryString: String,
        @Query("type-filter") typeFilter: String = "inetnum",
        // @Query("limit") ofset: Int = 10,
        @Query("flags") flags: String = "no-referenced"
    ): Response<MainDto>

    //   https://rest.db.ripe.net/ripe/organisation/ORG-JR8-RIPE.json
    @GET("ripe/organisation/{org}.json")
    suspend fun getOrganizationByIdentificator(
        @Path("org") orgIdentificator: String
    ): Response<MainDto>


    @GET("search.json")
    suspend fun getOrganizationByIp(
        @Query("source") source: String = "ripe",
        @Query("query-string") queryString: String,
        @Query("type-filter") typeFilter: String = "inetnum",
        // @Query("limit") ofset: Int = 10,
        @Query("flags") flags: String = "no-referenced"
    ): Response<MainDto>

}
