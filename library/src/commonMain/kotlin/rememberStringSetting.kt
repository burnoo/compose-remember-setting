package dev.burnoo.compose.remembersetting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import dev.burnoo.compose.remembersetting.internal.rememberSetting

@Composable
fun rememberStringSetting(key: String, defaultValue: String): MutableState<String> {
    return rememberSetting(key, defaultValue)
}

@Composable
fun rememberStringSettingOrNull(key: String): MutableState<String?> {
    return rememberSetting<String?>(key, defaultValue = null)
}
