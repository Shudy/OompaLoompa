package com.eliasortiz.oompaloomparrhh.data.models

import com.google.gson.annotations.SerializedName

data class OompaLoompa(

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
    val favorite: Favorite? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("age")
    val age: Int? = null,

    @field:SerializedName("height")
    val height: Int? = null
)