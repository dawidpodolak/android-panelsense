package com.panelsense.domain.interactor

import com.panelsense.domain.repository.UserDataRepository
import javax.inject.Inject

class LoginInteractor @Inject constructor(
    private val userDataRepository: UserDataRepository
) {
    suspend fun isUserLoggedIn(): Boolean = userDataRepository.isUserLoggedIn()
}
