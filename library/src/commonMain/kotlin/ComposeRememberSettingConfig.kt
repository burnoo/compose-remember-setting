package dev.burnoo.compose.remembersetting

import androidx.compose.runtime.compositionLocalOf
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.observable.makeObservable
import kotlinx.coroutines.CoroutineDispatcher

val LocalComposeRememberSettingConfig = compositionLocalOf { ComposeRememberSettingConfig() }

@OptIn(ExperimentalSettingsApi::class)
data class ComposeRememberSettingConfig(
    val observableSettings: ObservableSettings = Settings().makeObservable(),
    /**
     * Default null value uses [com.russhwolf.settings.coroutines.converterDefaultDispatcher]
     * which is Dispatchers.Default for wasm and Dispatchers.IO for all others platforms
     */
    val flowSettingsDispatcher: CoroutineDispatcher? = null
)
