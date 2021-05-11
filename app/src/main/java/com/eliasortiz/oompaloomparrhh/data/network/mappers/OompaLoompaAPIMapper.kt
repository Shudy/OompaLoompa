package com.eliasortiz.oompaloomparrhh.data.network.mappers

import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompaModel
import com.eliasortiz.oompaloomparrhh.data.network.models.OompaLoompaModelAPI
import com.eliasortiz.oompaloomparrhh.utils.ModelsMapper
import javax.inject.Inject

class OompaLoompaAPIMapper
@Inject constructor(private val favoriteMapper: FavoriteAPIMapper) :
    ModelsMapper<OompaLoompaModel, OompaLoompaModelAPI> {

    override fun mapToModel(domainModel: OompaLoompaModelAPI): OompaLoompaModel {
        return OompaLoompaModel(
            domainModel.id ?: -1,
            domainModel.profession ?: "",
            domainModel.image ?: "",
            domainModel.country ?: "",
            domainModel.gender ?: "",
            domainModel.lastName ?: "",
            domainModel.firstName ?: "",
            favoriteMapper.mapToModel(domainModel.favorite),
            domainModel.email ?: "",
            domainModel.age ?: 0,
            domainModel.height ?: 0,
            domainModel.quota ?: "",
            domainModel.description ?: ""
        )
    }

    override fun mapFromModel(model: OompaLoompaModel): OompaLoompaModelAPI {
        return OompaLoompaModelAPI(
            model.id,
            model.profession,
            model.image,
            model.country,
            model.gender,
            model.lastName,
            model.firstName,
            favoriteMapper.mapFromModel(model.favorite),
            model.email,
            model.age,
            model.height,
            model.quota,
            model.description
        )
    }

    fun mapFromDomainModelList(domainModelList: List<OompaLoompaModelAPI>): List<OompaLoompaModel> {
        return domainModelList.map { mapToModel(it) }
    }
}