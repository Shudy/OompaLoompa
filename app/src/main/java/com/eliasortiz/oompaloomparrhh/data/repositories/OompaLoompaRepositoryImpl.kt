package com.eliasortiz.oompaloomparrhh.data.repositories

import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompaModel
import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompasResponseModel
import com.eliasortiz.oompaloomparrhh.data.network.OompaLoompaService
import com.eliasortiz.oompaloomparrhh.data.network.SafeApiRequest
import com.eliasortiz.oompaloomparrhh.data.network.mappers.OompaLoompaAPIMapper
import com.eliasortiz.oompaloomparrhh.data.network.mappers.OompaLoompasResponseAPIMapper
import com.eliasortiz.oompaloomparrhh.data.network.models.OompaLoompaModelAPI
import com.eliasortiz.oompaloomparrhh.data.network.models.OompaLoompasResponseModelAPI
import com.eliasortiz.oompaloomparrhh.utils.ResultResponse
import javax.inject.Inject

class OompaLoompaRepositoryImpl
@Inject constructor(
    private val api: OompaLoompaService,
    private val oompaLoompaAPIMapper: OompaLoompaAPIMapper,
    private val oompaLoompaResponseAPIMapper: OompaLoompasResponseAPIMapper
) : OompaLoompaRepository, SafeApiRequest() {

    override suspend fun getOompaLoompasList(page: Int): ResultResponse<OompaLoompasResponseModel> {
        val networkResponse = apiRequest { api.getOompaLoompas(page) }
        when (networkResponse) {
            is ResultResponse.Loading -> {
                return networkResponse
            }

            is ResultResponse.Failure -> {
                return networkResponse
            }

            is ResultResponse.Success -> {
                val dataResponseApi = networkResponse.data as OompaLoompasResponseModelAPI

                val dataResponse = oompaLoompaResponseAPIMapper.mapToModel(dataResponseApi)

                return ResultResponse.Success(dataResponse)
            }
        }
    }

    override suspend fun getOompaLoompa(id: Int): ResultResponse<OompaLoompaModel> {
        val responseApi = apiRequest { api.getOompaLoompa(id) }

        return when (responseApi) {
            is ResultResponse.Failure -> responseApi
            is ResultResponse.Loading -> responseApi
            is ResultResponse.Success -> {
                ResultResponse.Success(
                    oompaLoompaAPIMapper.mapToModel(responseApi.data as OompaLoompaModelAPI)
                )
            }
        }
    }
}