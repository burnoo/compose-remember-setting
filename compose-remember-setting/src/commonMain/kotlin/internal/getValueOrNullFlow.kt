package dev.burnoo.compose.remembersetting.internal

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalSettingsApi::class)
internal inline fun <reified T> FlowSettings.getValueOrNullFlow(key: String): Flow<T?> = when (T::class) {
    Int::class -> getIntOrNullFlow(key)
    Long::class -> getLongOrNullFlow(key)
    String::class -> getStringOrNullFlow(key)
    Float::class -> getFloatOrNullFlow(key)
    Double::class -> getDoubleOrNullFlow(key)
    Boolean::class -> getBooleanOrNullFlow(key)
    else -> throw IllegalArgumentException("Invalid type!")
} as Flow<T?>
