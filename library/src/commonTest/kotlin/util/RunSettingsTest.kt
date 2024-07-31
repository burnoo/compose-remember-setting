package dev.burnoo.compose.remembersetting.util

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.russhwolf.settings.ObservableSettings

@OptIn(ExperimentalTestApi::class)
fun <T> runSettingsTest(
    settings: ObservableSettings,
    block: SettingsComposeUiTest<T>.() -> Unit
) = runComposeUiTest {
    val settingsComposeUiTest = SettingsComposeUiTest<T>(this, settings)
    settingsComposeUiTest.block()
}
