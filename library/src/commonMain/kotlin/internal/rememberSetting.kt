package dev.burnoo.compose.remembersetting.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.set
import dev.burnoo.compose.remembersetting.LocalComposeRememberSettingConfig

@OptIn(ExperimentalSettingsApi::class)
@Composable
internal inline fun <reified T> rememberSetting(key: String, defaultValue: T): MutableState<T> {
    val (observableSettings, flowSettingsDispatcher) = LocalComposeRememberSettingConfig.current
    val coroutineScope = rememberCoroutineScope()
    return remember {
        val flowSettings = if (flowSettingsDispatcher == null) {
            observableSettings.toFlowSettings()
        } else {
            observableSettings.toFlowSettings(flowSettingsDispatcher)
        }
        flowSettings.getStateFlow(coroutineScope, key, defaultValue)
            .asMutableState(coroutineScope, setValue = { observableSettings[key] = it })
    }
}
