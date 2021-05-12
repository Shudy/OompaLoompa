package com.eliasortiz.oompaloomparrhh.data.network.mappers

import com.eliasortiz.oompaloomparrhh.data.models.FavoriteModel
import com.eliasortiz.oompaloomparrhh.data.network.models.FavoriteModelAPI
import com.eliasortiz.oompaloomparrhh.utils.ModelsMapper
import javax.inject.Inject

class FavoriteAPIMapper
@Inject constructor() : ModelsMapper<FavoriteModel, FavoriteModelAPI?> {
    override fun mapToModel(domainModel: FavoriteModelAPI?): FavoriteModel {
        return domainModel?.let {
            FavoriteModel(
                domainModel.song ?: "",
                domainModel.randomString ?: "",
                domainModel.color ?: "",
                domainModel.food ?: ""
            )
        } ?: run {
            FavoriteModel(
                song = "",
                randomString = "",
                color = "",
                food = ""
            )
        }
    }

    override fun mapFromModel(model: FavoriteModel): FavoriteModelAPI {
        return FavoriteModelAPI(
            model.song,
            model.randomString,
            model.color,
            model.food
        )
    }
}