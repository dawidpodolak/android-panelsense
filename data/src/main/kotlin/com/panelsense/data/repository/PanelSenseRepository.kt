package com.panelsense.data.repository

import android.util.Base64
import com.google.gson.Gson
import com.panelsense.core.AppDataProvider
import com.panelsense.core.VersionDataProvider
import com.panelsense.data.api.WebsocketConnectionProvider
import com.panelsense.data.model.AuthDataModel
import com.panelsense.data.model.AuthModelRequest
import com.panelsense.data.model.AuthResultModel
import com.panelsense.data.model.MessageType
import com.panelsense.domain.model.LoginSuccess
import com.panelsense.domain.model.ServerConnectionData
import com.panelsense.domain.repository.ServerRepository
import kotlinx.coroutines.flow.first
import java.nio.charset.Charset
import javax.inject.Inject

class PanelSenseRepository @Inject constructor(
    private val connectionProvider: WebsocketConnectionProvider,
    private val versionDataProvider: VersionDataProvider,
    private val appDataProvider: AppDataProvider,
    private val gson: Gson
) : ServerRepository {

    @Suppress("MagicNumber")
    override suspend fun login(serverConnectionData: ServerConnectionData): Result<LoginSuccess> {
        connectionProvider.connectToClient(
            serverConnectionData.serverAddressIp,
            serverConnectionData.serverPort
        )
            .getOrNull() ?: return Result.failure(IllegalStateException("Connection failed"))
        return loginToPanelSenseAddon(serverConnectionData)
    }

    private suspend fun loginToPanelSenseAddon(serverConnectionData: ServerConnectionData): Result<LoginSuccess> {
        val authData = AuthDataModel(
            token = serverConnectionData.getToken(),
            version_name = versionDataProvider.versionName,
            version_code = versionDataProvider.versionCode,
            name = serverConnectionData.panelSenseName,
            installation_id = appDataProvider.installationId()
        )

        connectionProvider.sendMessage(AuthModelRequest(data = authData))

        return runCatching {
            val message = connectionProvider.listenForMessages().first()

            if (message.type != MessageType.AUTH) {
                throw IllegalStateException("Expected AUTH message, got $message")
            }

            val loginResult = gson.fromJson(message.data.toString(), AuthResultModel::class.java)
            if (loginResult.auth_result == AuthResultModel.Result.FAILURE) {
                throw IllegalStateException("Login failed")
            }
            LoginSuccess
        }
    }

    private fun ServerConnectionData.getToken(): String {
        val token = "$userName:$password"
        return Base64.encodeToString(token.toByteArray(Charset.defaultCharset()), Base64.DEFAULT)
    }
}
