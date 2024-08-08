package dev.burnoo.compose.remembersetting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalInspectionMode
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.observable.makeObservable

/**
 * CompositionLocal to provide [ComposeRememberSettingConfig] to all children.
 * By default uses [LocalDefaultComposeRememberSettingConfig] in runtime and
 * [LocalPreviewComposeRememberSettingConfig] in inspection mode (e.g. preview).
 * To get non-null current value use [ProvidableCompositionLocal.requireCurrent].
 */
val LocalComposeRememberSettingConfig = compositionLocalOf<ComposeRememberSettingConfig?> { null }

@Composable
fun ProvidableCompositionLocal<ComposeRememberSettingConfig?>.requireCurrent(): ComposeRememberSettingConfig {
    val isLocalInspectionMode = LocalInspectionMode.current
    val localComposeRememberSettingConfig = current
    return when {
        localComposeRememberSettingConfig != null -> localComposeRememberSettingConfig
        isLocalInspectionMode -> LocalPreviewComposeRememberSettingConfig.current
        else -> LocalDefaultComposeRememberSettingConfig.current
    }
}

@OptIn(ExperimentalSettingsApi::class)
private val LocalDefaultComposeRememberSettingConfig = staticCompositionLocalOf {
    ComposeRememberSettingConfig(observableSettings = Settings().makeObservable())
}

private val LocalPreviewComposeRememberSettingConfig = staticCompositionLocalOf {
    ComposeRememberSettingConfig(observableSettings = MapSettings())
}
