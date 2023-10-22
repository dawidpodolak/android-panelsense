package com.panelsense.domain.model

data class ServerConnectionData(
    val serverAddressIp: String,
    val serverPort: String,
    val userName: String,
    val password: String
)
