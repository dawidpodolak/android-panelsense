package com.panelsense.data.model

data class AuthModelRequest(
    val type: MessageType = MessageType.AUTH,
    val data: Any
)

data class AuthDataModel(
    val token: String,
    val versionName: String,
    val versionCode: Int,
    val name: String,
    val installationId: String
)

data class AuthResultModel(
    val authResult: Result
) {
    enum class Result {
        SUCCESS,
        FAILURE
    }
}
