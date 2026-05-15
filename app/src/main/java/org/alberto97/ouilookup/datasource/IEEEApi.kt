package org.alberto97.ouilookup.datasource

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface IEEEApi {

    @Headers("User-Agent: ")
    @GET("oui/oui.csv")
    suspend fun fetchOui(): Response<String>
}