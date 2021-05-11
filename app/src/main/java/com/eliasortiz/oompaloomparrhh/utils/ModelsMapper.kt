package com.eliasortiz.oompaloomparrhh.utils

interface ModelsMapper<Model, DomainModel> {
    fun mapToModel(domainModel: DomainModel): Model
    fun mapFromModel(model: Model): DomainModel
}