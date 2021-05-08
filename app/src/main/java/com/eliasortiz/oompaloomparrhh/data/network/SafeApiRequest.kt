package com.eliasortiz.oompaloomparrhh.data.network

import com.eliasortiz.oompaloomparrhh.utils.ResultResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): ResultResponse<Any> {
        val response = call.invoke()

        if (response.isSuccessful) {

            response.body()?.let { body ->
                return ResultResponse.Success(body)
            } ?: run {
                return ResultResponse.Failure(message = "Empty body")
            }

        } else {
            val error = response.errorBody()?.string()
            val message = StringBuilder()

            error?.let {
                try {
                    message.appendLine(JSONObject(it).getString("message"))
                } catch (e: JSONException) {
                    message.appendLine("Unknown message error")
                }
            }

            return ResultResponse.Failure(response.code().toString(), message.toString())
        }
    }
}