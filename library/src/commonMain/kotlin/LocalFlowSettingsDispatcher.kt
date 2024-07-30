package dev.burnoo.compose.remembersetting

import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Default null value uses [com.russhwolf.settings.coroutines.converterDefaultDispatcher]
 * which is Dispatchers.Default for wasm and Dispatchers.IO for all others platforms
 */
val LocalFlowSettingsDispatcher = compositionLocalOf<CoroutineDispatcher?> { null }
