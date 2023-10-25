@file:Suppress("ConstructorParameterNaming")

package com.panelsense.data.model

data class AuthModelRequest(
    val type: MessageType = MessageType.AUTH,
    val data: Any
)

data class AuthDataModel(
    val token: String,
    val version_name: String,
    val version_code: Int,
    val name: String,
    val installation_id: String
)

data class AuthResultModel(
    val auth_result: Result
) {
    enum class Result {
        SUCCESS,
        FAILURE
    }
}
