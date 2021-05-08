package com.eliasortiz.oompaloomparrhh.data.network

import com.eliasortiz.oompaloomparrhh.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OompaLoompaApi {

    private var instance: OompaLoompaService? = null

    fun getInstance(): OompaLoompaService {

        instance?.let {
            return it
        } ?: run {

            val clientBuilder = OkHttpClient.Builder()

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BASIC
                clientBuilder.addInterceptor(logging)
            }

            instance = Retrofit
                .Builder()
                .baseUrl(BuildConfig.BACKEND_ENDPOINT_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build()
                .create(OompaLoompaService::class.java)

            return instance!!
        }
    }
}