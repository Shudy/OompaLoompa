package com.eliasortiz.oompaloomparrhh.data.network.mappers

import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompasResponseModel
import com.eliasortiz.oompaloomparrhh.data.network.models.OompaLoompasResponseModelAPI
import com.eliasortiz.oompaloomparrhh.utils.ModelsMapper
import javax.inject.Inject

class OompaLoompasResponseAPIMapper
@Inject constructor(private val oompaLoompaAPIMapper: OompaLoompaAPIMapper) :
    ModelsMapper<OompaLoompasResponseModel, OompaLoompasResponseModelAPI> {

    override fun mapToModel(domainModel: OompaLoompasResponseModelAPI): OompaLoompasResponseModel {
        return OompaLoompasResponseModel(
            domainModel.current ?: -1,
            domainModel.total ?: -1,
            oompaLoompaAPIMapper.mapFromDomainModelList(
                domainModel.results?.filterNotNull() ?: emptyList()
            )
        )
    }

    override fun mapFromModel(model: OompaLoompasResponseModel): OompaLoompasResponseModelAPI {
        return OompaLoompasResponseModelAPI(
            model.current,
            model.total,
            model.results.map { oompaLoompaAPIMapper.mapFromModel(it) }
        )
    }
}