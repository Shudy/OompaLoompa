package com.eliasortiz.oompaloomparrhh.data.repositories

import com.eliasortiz.oompaloomparrhh.data.network.OompaLoompaService
import com.eliasortiz.oompaloomparrhh.data.network.SafeApiRequest

class OompaLoompaRepository(
    private val api: OompaLoompaService
) : SafeApiRequest() {

    suspend fun getOompaLoompasList(page: Int) = apiRequest { api.getOompaLoompas(page) }

    suspend fun getOompaLoompa(id: Int) = apiRequest { api.getOompaLoompa(id) }

    companion object {
        private var instance: OompaLoompaRepository? = null

        fun getInstance(api: OompaLoompaService): OompaLoompaRepository {
            instance?.let {
                return it
            } ?: run {
                instance = OompaLoompaRepository(api)
                return instance!!
            }
        }
    }
}