package com.eliasortiz.oompaloomparrhh.data.models

import com.google.gson.annotations.SerializedName

data class Favorite(

    @field:SerializedName("song")
    val song: String? = null,

    @field:SerializedName("random_string")
    val randomString: String? = null,

    @field:SerializedName("color")
    val color: String? = null,

    @field:SerializedName("food")
    val food: String? = null
)