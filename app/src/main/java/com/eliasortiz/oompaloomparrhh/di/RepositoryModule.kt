package com.eliasortiz.oompaloomparrhh.di

import com.eliasortiz.oompaloomparrhh.data.network.OompaLoompaService
import com.eliasortiz.oompaloomparrhh.data.network.mappers.OompaLoompaAPIMapper
import com.eliasortiz.oompaloomparrhh.data.network.mappers.OompaLoompasResponseAPIMapper
import com.eliasortiz.oompaloomparrhh.data.repositories.OompaLoompaRepository
import com.eliasortiz.oompaloomparrhh.data.repositories.OompaLoompaRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        api: OompaLoompaService,
        oompaLoompaAPIMapper: OompaLoompaAPIMapper,
        oompaLoompaResponseAPIMapper: OompaLoompasResponseAPIMapper
    ): OompaLoompaRepository {
        return OompaLoompaRepositoryImpl(
            api,
            oompaLoompaAPIMapper,
            oompaLoompaResponseAPIMapper
        )
    }
}