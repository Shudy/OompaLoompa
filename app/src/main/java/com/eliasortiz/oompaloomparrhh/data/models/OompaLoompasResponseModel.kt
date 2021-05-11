package com.eliasortiz.oompaloomparrhh.data.models

data class OompaLoompasResponseModel(
    val current: Int,
    val total: Int,
    val results: List<OompaLoompaModel>
)