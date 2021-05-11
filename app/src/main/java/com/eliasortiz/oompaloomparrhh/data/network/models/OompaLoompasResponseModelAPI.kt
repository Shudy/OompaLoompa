package com.eliasortiz.oompaloomparrhh.data.network.models

import com.google.gson.annotations.SerializedName

data class OompaLoompasResponseModelAPI(

    @field:SerializedName("current")
    val current: Int? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("results")
    val results: List<OompaLoompaModelAPI?>? = null

)