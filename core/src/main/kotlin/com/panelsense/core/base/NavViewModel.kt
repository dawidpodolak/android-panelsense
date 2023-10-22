package com.panelsense.core.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull

open class NavViewModel<NavCommand> : ViewModel() {

    private val _navCommandFlow = MutableStateFlow<NavCommand?>(null)

    protected fun navigateTo(navCommand: NavCommand) {
        _navCommandFlow.value = navCommand
    }

    suspend fun collectNavCommand(callback: (NavCommand) -> Unit) =
        _navCommandFlow.mapNotNull { it }
            .collect { callback(it) }
}
