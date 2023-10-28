package com.panelsense.app.di

import com.panelsense.app.BuildConfig
import com.panelsense.core.VersionDataProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideVersionDataProvider(): VersionDataProvider = object : VersionDataProvider {
        override val versionName: String = BuildConfig.VERSION_NAME
        override val versionCode: Int = BuildConfig.VERSION_CODE
    }
}
