package com.panelsense.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.panelsense.core.di.DataStoreType.Type.SvgImage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {

    private val Context.dataStore by preferencesDataStore(IMAGE_SVG_DATA_STORE)

    @Provides
    @DataStoreType(SvgImage)
    fun providesImageDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore

    companion object {
        const val IMAGE_SVG_DATA_STORE = "image_data_store"
    }
}
