package dev.burnoo.compose.remembersetting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import dev.burnoo.compose.remembersetting.internal.rememberSetting

@Composable
fun rememberLongSetting(key: String, defaultValue: Long): MutableState<Long> {
    return rememberSetting(key, defaultValue)
}

@Composable
fun rememberLongSettingOrNull(key: String): MutableState<Long?> {
    return rememberSetting<Long?>(key, defaultValue = null)
}
