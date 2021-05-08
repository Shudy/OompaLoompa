package com.eliasortiz.oompaloomparrhh.data.network

import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompa
import com.eliasortiz.oompaloomparrhh.data.network.reponse.OompaLoompasResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OompaLoompaService {

    @GET("oompa-loompas")
    suspend fun getOompaLoompas(
        @Query("page") page: Int = 1
    ): Response<OompaLoompasResponse>

    @GET("oompa-loompas/{uid}")
    suspend fun getOompaLoompa(
        @Path("uid") oompaLoompaId: Int
    ): Response<OompaLoompa>

}