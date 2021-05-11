package com.eliasortiz.oompaloomparrhh.di

import android.content.Context
import com.eliasortiz.oompaloomparrhh.BuildConfig
import com.eliasortiz.oompaloomparrhh.data.network.NetworkConnectionInterception
import com.eliasortiz.oompaloomparrhh.data.network.OompaLoompaService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideNetworkConnectionInterceptor(@ApplicationContext context: Context): NetworkConnectionInterception {
        return NetworkConnectionInterception(context)
    }

    @Singleton
    @Provides
    fun provideOkHttpBuilder(networkConnectionInterceptor: NetworkConnectionInterception): OkHttpClient.Builder {
        val clientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            clientBuilder.addInterceptor(logging)
        }

        clientBuilder.addInterceptor(networkConnectionInterceptor)

        return clientBuilder
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClientBuilder: OkHttpClient.Builder): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_ENDPOINT_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
    }

    @Singleton
    @Provides
    fun provideOompaLoompaService(retrofit: Retrofit.Builder): OompaLoompaService {
        return retrofit
            .build()
            .create(OompaLoompaService::class.java)
    }
}