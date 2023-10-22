package com.panelsense.domain.repository

interface UserDataRepository {
    suspend fun isUserLoggedIn(): Boolean
}
