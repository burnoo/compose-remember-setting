package dev.burnoo.compose.remembersetting.internal

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

internal inline fun <reified T> StateFlow<T>.asMutableState(
    coroutineScope: CoroutineScope,
    crossinline setValue: suspend (T) -> Unit,
): MutableState<T> {
    val stateFlow = this
    val snapshotMutableState = mutableStateOf(stateFlow.value)
    coroutineScope.launch {
        stateFlow.collectLatest {
            withContext(Dispatchers.Main) { snapshotMutableState.value = it }
        }
    }
    var setValueJob: Job? = null
    return object : MutableState<T> {
        override var value: T
            get() = snapshotMutableState.value
            set(value) {
                snapshotMutableState.value = value
                setValueJob?.cancel()
                setValueJob = coroutineScope.launch { setValue(value) }
            }

        override fun component1(): T = value
        override fun component2(): (T) -> Unit = { value = it }
    }
}
