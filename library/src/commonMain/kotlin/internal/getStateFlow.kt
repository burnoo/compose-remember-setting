package dev.burnoo.compose.remembersetting.internal

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalSettingsApi::class)
internal inline fun <reified T> FlowSettings.getStateFlow(
    coroutineScope: CoroutineScope,
    key: String,
    defaultValue: T,
) = getValueOrNullFlow<T>(key)
    .filterNotNull()
    .stateIn(coroutineScope, started = SharingStarted.Eagerly, defaultValue)
