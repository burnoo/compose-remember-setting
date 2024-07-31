package dev.burnoo.compose.remembersetting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import dev.burnoo.compose.remembersetting.internal.rememberSetting

@Composable
fun rememberFloatSetting(key: String, defaultValue: Float): MutableState<Float> {
    return rememberSetting(key, defaultValue)
}

@Composable
fun rememberFloatSettingOrNull(key: String): MutableState<Float?> {
    return rememberSetting<Float?>(key, defaultValue = null)
}
