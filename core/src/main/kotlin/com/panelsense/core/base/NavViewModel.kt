package com.panelsense.core.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull

abstract class NavViewModel<NavCommand, ViewState> : ViewModel() {

    abstract fun defaultState(): ViewState

    private val _stateFlow = MutableStateFlow(defaultState())
    val stateFlow: StateFlow<ViewState> = _stateFlow

    private val _errorFlow = MutableStateFlow<Throwable?>(null)
    val errorStatFlow: StateFlow<Throwable?> = _errorFlow

    private val _navCommandFlow = MutableStateFlow<NavCommand?>(null)

    protected fun navigateTo(navCommand: NavCommand) {
        _navCommandFlow.value = navCommand
    }

    protected fun showError(throwable: Throwable) {
        _errorFlow.value = throwable
    }

    fun clearError() {
        _errorFlow.value = null
    }

    fun modify(modifier: ViewState.() -> ViewState) {
        val currentState = _stateFlow.value
        val newState = currentState.modifier()
        setState(newState)
    }

    private fun setState(state: ViewState) {
        _stateFlow.value = state
    }

    suspend fun collectNavCommand(callback: (NavCommand) -> Unit) =
        _navCommandFlow.mapNotNull { it }
            .collect { callback(it) }

    suspend fun collectError(callback: (Throwable) -> Unit) =
        _errorFlow.mapNotNull { it }
            .collect { callback(it) }
}
