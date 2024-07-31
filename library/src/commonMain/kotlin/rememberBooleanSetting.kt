package dev.burnoo.compose.remembersetting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import dev.burnoo.compose.remembersetting.internal.rememberSetting

@Composable
fun rememberBooleanSetting(key: String, defaultValue: Boolean): MutableState<Boolean> {
    return rememberSetting(key, defaultValue)
}

@Composable
fun rememberBooleanSettingOrNull(key: String): MutableState<Boolean?> {
    return rememberSetting<Boolean?>(key, defaultValue = null)
}
