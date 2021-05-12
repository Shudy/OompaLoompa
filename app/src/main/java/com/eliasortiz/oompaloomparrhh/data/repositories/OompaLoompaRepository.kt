package com.eliasortiz.oompaloomparrhh.data.repositories

import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompaModel
import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompasResponseModel
import com.eliasortiz.oompaloomparrhh.utils.ResultResponse

interface OompaLoompaRepository {

    suspend fun getOompaLoompasList(page: Int): ResultResponse<OompaLoompasResponseModel>

    suspend fun getOompaLoompa(id: Int): ResultResponse<OompaLoompaModel>

}
