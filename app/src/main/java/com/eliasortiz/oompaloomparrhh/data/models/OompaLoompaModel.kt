package com.eliasortiz.oompaloomparrhh.data.models

class OompaLoompaModel(
    val id: Int,
    val profession: String,
    val image: String,
    val country: String,
    val gender: String,
    val lastName: String,
    val firstName: String,
    val favorite: FavoriteModel,
    val email: String,
    val age: Int,
    val height: Int,
    val quota: String,
    val description: String
)