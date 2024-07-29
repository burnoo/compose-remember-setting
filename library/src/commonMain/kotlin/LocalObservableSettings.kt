package dev.burnoo.compose.remembersetting

import androidx.compose.runtime.compositionLocalOf
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.observable.makeObservable

@OptIn(ExperimentalSettingsApi::class)
val LocalObservableSettings = compositionLocalOf { Settings().makeObservable() }
