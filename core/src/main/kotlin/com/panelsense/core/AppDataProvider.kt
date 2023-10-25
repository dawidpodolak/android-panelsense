package com.panelsense.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.panelsense.core.di.DataStoreType
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

class AppDataProvider @Inject constructor(
    @DataStoreType(DataStoreType.Type.AppData)
    private val appDataStore: DataStore<Preferences>
) {
    suspend fun installationId(): String {
        var installationId: String? = appDataStore.data.firstOrNull()?.get(INSTALLATION_ID)

        if (installationId == null) {
            installationId = UUID.randomUUID().toString()
            appDataStore.edit {
                it[INSTALLATION_ID] = installationId
            }
        }
        return installationId
    }

    private companion object {
        val INSTALLATION_ID = stringPreferencesKey("installation_id")
    }
}
