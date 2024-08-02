package dev.burnoo.compose.remembersetting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import dev.burnoo.compose.remembersetting.internal.rememberSetting

@Composable
fun rememberIntSetting(key: String, defaultValue: Int): MutableState<Int> {
    return rememberSetting(key, defaultValue)
}

@Composable
fun rememberIntSettingOrNull(key: String): MutableState<Int?> {
    return rememberSetting<Int?>(key, defaultValue = null)
}
