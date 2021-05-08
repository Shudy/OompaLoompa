package com.eliasortiz.oompaloomparrhh.data.network.reponse

import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompa
import com.google.gson.annotations.SerializedName

data class OompaLoompasResponse(

    @field:SerializedName("current")
    val current: Int? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("results")
    val results: List<OompaLoompa?>? = null

)