package com.nspanel.sense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nspanel.core.model.SenseConfiguration
import com.nspanel.data.repository.ConfigurationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val configRepository: ConfigurationRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(MainViewState())
    val stateFlow: StateFlow<MainViewState> = _stateFlow

    init {
        loadConfiguration()
    }

    private fun loadConfiguration() {
        viewModelScope.launch {
            val senseConfig = configRepository.getConfiguration()
            Timber.d("Sense config: $senseConfig")
            _stateFlow.value = MainViewState(senseConfig)
        }
    }

    data class MainViewState(
        val senseConfiguration: SenseConfiguration? = null
    )
}
