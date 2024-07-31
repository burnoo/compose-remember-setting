package dev.burnoo.compose.remembersetting

import androidx.compose.runtime.LaunchedEffect
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import dev.burnoo.compose.remembersetting.util.runSettingsTest
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RememberStringSettingTest {

    private val settings = MapSettings()
    private val key = "key"

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldRememberStringSetting() = runSettingsTest<String>(settings) {
        rememberMutableState = { rememberStringSetting(key, "defaultValue") }

        setUpContent()

        assertString("defaultValue")
        settings[key] = "newValue"
        assertString("newValue")
    }

    @Test
    fun shouldRememberStringSettingOrNull() = runSettingsTest<String?>(settings) {
        rememberMutableState = { rememberStringSettingOrNull(key) }

        setUpContent()

        assertString("null")
        settings[key] = "newValue"
        assertString("newValue")
    }

    @Test
    fun shouldUpdateStringSetting() = runSettingsTest<String>(settings) {
        rememberMutableState = { rememberStringSetting(key, "defaultValue") }

        setUpContent { mutableState ->
            LaunchedEffect(Unit) {
                mutableState.value = "newValue"
            }
        }

        assertString("newValue")
        settings.get<String>(key) shouldBe "newValue"
    }

    @Test
    fun shouldUpdateStringSettingOrNull() = runSettingsTest<String?>(settings) {
        rememberMutableState = { rememberStringSettingOrNull(key) }

        setUpContent { mutableState ->
            LaunchedEffect(Unit) {
                mutableState.value = "newValue"
            }
        }

        assertString("newValue")
        settings.get<String>(key) shouldBe "newValue"
    }

    @Test
    fun shouldSyncTwoStringSettings() = runSettingsTest<String>(settings) {
        rememberMutableState = { rememberStringSetting(key, "defaultValue") }

        setUpContent {
            val secondStringSetting = rememberStringSetting(key, "defaultValue2")
            LaunchedEffect(Unit) {
                secondStringSetting.value = "newValue"
            }
        }

        assertString("newValue")
        settings.get<String>(key) shouldBe "newValue"
    }
}

