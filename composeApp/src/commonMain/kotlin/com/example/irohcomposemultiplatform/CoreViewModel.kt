package com.example.irohcomposemultiplatform

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uniffi.irohcompose.Core
import uniffi.irohcompose.EventHandler
import uniffi.irohcompose.Model

class CoreViewModel : ViewModel(), EventHandler {
    private val _instance = Core(this)
    val instance: Core
        get() = _instance

    private val _state = MutableStateFlow<Model?>(null)
    val state: StateFlow<Model?> = _state

    override fun onUpdate(model: Model) {
        _state.value = model
    }
}
