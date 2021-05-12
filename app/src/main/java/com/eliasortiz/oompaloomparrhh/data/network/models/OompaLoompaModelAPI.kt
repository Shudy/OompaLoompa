package com.eliasortiz.oompaloomparrhh.data.network.models

import com.google.gson.annotations.SerializedName

data class OompaLoompaModelAPI(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("profession")
    val profession: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("last_name")
    val lastName: String? = null,

    @field:SerializedName("first_name")
    val firstName: String? = null,

    @field:SerializedName("favorite")
    val favorite: FavoriteModelAPI? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("age")
    val age: Int? = null,

    @field:SerializedName("height")
    val height: Int? = null,

    @field:SerializedName("quota")
    val quota: String? = null,

    @field:SerializedName("description")
    val description: String? = null
)