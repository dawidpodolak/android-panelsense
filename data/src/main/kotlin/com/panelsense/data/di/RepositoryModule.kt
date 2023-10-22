package com.panelsense.data.di

import com.panelsense.data.repository.HomeAssistantRepository
import com.panelsense.data.repository.StorageUserDataRepository
import com.panelsense.domain.repository.ServerRepository
import com.panelsense.domain.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindUserDataRepository(storageUserDataRepository: StorageUserDataRepository): UserDataRepository

    @Binds
    fun bindServerRepository(haRepository: HomeAssistantRepository): ServerRepository
}
