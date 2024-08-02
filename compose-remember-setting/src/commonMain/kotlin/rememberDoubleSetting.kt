package dev.burnoo.compose.remembersetting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import dev.burnoo.compose.remembersetting.internal.rememberSetting

@Composable
fun rememberDoubleSetting(key: String, defaultValue: Double): MutableState<Double> {
    return rememberSetting(key, defaultValue)
}

@Composable
fun rememberDoubleSettingOrNull(key: String): MutableState<Double?> {
    return rememberSetting<Double?>(key, defaultValue = null)
}
