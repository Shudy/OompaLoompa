package com.eliasortiz.oompaloomparrhh.utils


sealed class ResultResponse<out T : Any> {
    object Loading : ResultResponse<Nothing>()
    data class Success<out T : Any>(val data: T?) : ResultResponse<T>()
    data class Failure(
        val errorCode: String = "",
        val message: String = ""
    ) : ResultResponse<Nothing>()
}