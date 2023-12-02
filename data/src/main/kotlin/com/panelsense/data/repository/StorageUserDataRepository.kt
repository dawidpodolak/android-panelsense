package com.panelsense.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.panelsense.core.di.DataStoreType
import com.panelsense.domain.model.ServerConnectionData
import com.panelsense.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class StorageUserDataRepository @Inject constructor(
    @DataStoreType(DataStoreType.Type.AppData)
    private val appDataStore: DataStore<Preferences>
) : UserDataRepository {

    @Suppress("ReturnCount")
    override suspend fun getServerConnectionData(): ServerConnectionData? {
        appDataStore.data.firstOrNull() ?: return null
        return ServerConnectionData(
            serverIPAddress = appDataStore.data.firstOrNull()?.get(serverIPAddress) ?: return null,
            serverPort = appDataStore.data.firstOrNull()?.get(serverPort) ?: return null,
            panelSenseName = appDataStore.data.firstOrNull()?.get(panelSenseName) ?: return null,
            userName = appDataStore.data.firstOrNull()?.get(userName) ?: return null,
            password = appDataStore.data.firstOrNull()?.get(password) ?: return null
        )
    }

    override suspend fun saveServerConnectionData(serverConnectionData: ServerConnectionData) {
        appDataStore.edit {
            it[serverIPAddress] = serverConnectionData.serverIPAddress
            it[serverPort] = serverConnectionData.serverPort
            it[panelSenseName] = serverConnectionData.panelSenseName
            it[userName] = serverConnectionData.userName
            it[password] = serverConnectionData.password
        }
    }

    override fun clearData() {
        // TODO: implement cleanup data
    }

    companion object {
        val serverIPAddress = stringPreferencesKey("serverIPAddress")
        val serverPort = stringPreferencesKey("serverPort")
        val panelSenseName = stringPreferencesKey("panelSenseName")
        val userName = stringPreferencesKey("userName")
        val password = stringPreferencesKey("password")
    }
}
