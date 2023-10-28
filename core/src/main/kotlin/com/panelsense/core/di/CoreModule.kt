package com.panelsense.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.panelsense.core.di.DataStoreType.Type.AppData
import com.panelsense.core.di.DataStoreType.Type.SvgImage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {

    private val Context.svgDtaStore by preferencesDataStore(IMAGE_SVG_DATA_STORE)
    private val Context.dataStore by preferencesDataStore(APP_DATA_STORE)

    @Provides
    @DataStoreType(SvgImage)
    fun providesImageDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.svgDtaStore

    @Provides
    @DataStoreType(AppData)
    fun providesAppDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore

    companion object {
        const val IMAGE_SVG_DATA_STORE = "image_data_store"
        const val APP_DATA_STORE = "app_data_store"
    }
}
