package com.sample.onlinestore.commonmodule.foundation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * We need to pass the initial state through constructor
 */
abstract class BaseViewModel<S : State, A : Action, E : Event>(initialScreenState: S) :
    ViewModel() {

    private val scope: CoroutineScope = viewModelScope
    private val _uiState = MutableStateFlow(initialScreenState)
    val uiState: StateFlow<S>
        get() = _uiState.asStateFlow()

    private val _action = Channel<A>(Channel.BUFFERED)
    val action: Flow<A>
        get() = _action.receiveAsFlow()

    private val _uiEvent = Channel<E>(Channel.BUFFERED)

    val uiEvent: Flow<E>
        get() = _uiEvent.receiveAsFlow()

    init {
        dispatch()
    }

    abstract fun reduce(currentState: S, action: A): S
    abstract fun runSideEffect(action: A, currentState: S)

    fun sendState(state: S) {
        scope.launch {
            _uiState.value = state
        }
    }

    fun sendAction(action: A) {
        scope.launch {
            _action.send(action)
        }
    }

    fun sendEvent(event: E) {
        scope.launch {
            _uiEvent.send(event)
        }
    }

    private fun dispatch() {
        action.onEach {
            val newState = reduce(uiState.value, it)
            sendState(newState)
            runSideEffect(it, newState)
        }.launchIn(scope)
    }
}
