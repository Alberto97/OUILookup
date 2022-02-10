package org.alberto97.ouilookup.datasource

import retrofit2.Response
import retrofit2.http.GET

interface IEEEApi {

    @GET("oui/oui.csv")
    suspend fun fetchOui(): Response<String>
}