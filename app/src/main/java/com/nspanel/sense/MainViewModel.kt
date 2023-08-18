package com.nspanel.sense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nspanel.data.repository.ConfigurationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val configRepository: ConfigurationRepository
): ViewModel() {


    init {
        loadConfiguration()
    }
    private fun loadConfiguration() {
        viewModelScope.launch {
            val senseConfig = configRepository.getConfiguration()
            Timber.d("senseConfig: $senseConfig")
        }
    }
}
