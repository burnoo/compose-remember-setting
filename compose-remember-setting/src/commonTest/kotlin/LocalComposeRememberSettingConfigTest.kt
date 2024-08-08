package dev.burnoo.compose.remembersetting

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.russhwolf.settings.MapSettings
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeInstanceOf
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class LocalComposeRememberSettingConfigTest {

    @Test
    fun shouldUseMapSettingsInPreviewInPreview() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalInspectionMode provides true) {
                val config = LocalComposeRememberSettingConfig.requireCurrent()
                LaunchedEffect(Unit) {
                    config.observableSettings.shouldBeInstanceOf<MapSettings>()
                }
            }
        }
    }

    @Test
    fun shouldNotUseMapSettingsInPreviewByDefault() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalInspectionMode provides false) {
                val config = LocalComposeRememberSettingConfig.requireCurrent()
                LaunchedEffect(Unit) {
                    config.observableSettings.shouldNotBeInstanceOf<MapSettings>()
                }
            }
        }
    }

    @Test
    fun shouldUseProvidedSettings() = runComposeUiTest {
        val providedSettings = MapSettings()
        setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides false,
                LocalComposeRememberSettingConfig provides ComposeRememberSettingConfig(
                    observableSettings = providedSettings,
                ),
            ) {
                val config = LocalComposeRememberSettingConfig.requireCurrent()
                LaunchedEffect(Unit) {
                    config.observableSettings shouldBeSameInstanceAs providedSettings
                }
            }
        }
    }
}
