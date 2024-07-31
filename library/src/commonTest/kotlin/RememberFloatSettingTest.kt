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
class RememberFloatSettingTest {

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
    fun shouldRememberFloatSetting() = runSettingsTest<Float>(settings) {
        rememberMutableState = { rememberFloatSetting(key, 0f) }

        setUpContent()

        assertString(0f.toString())
        settings[key] = 1f
        assertString(1f.toString())
    }

    @Test
    fun shouldRememberFloatSettingOrNull() = runSettingsTest<Float?>(settings) {
        rememberMutableState = { rememberFloatSettingOrNull(key) }

        setUpContent()

        assertString("null")
        settings[key] = 1f
        assertString(1f.toString())
    }

    @Test
    fun shouldUpdateStringSetting() = runSettingsTest<Float>(settings) {
        rememberMutableState = { rememberFloatSetting(key, 0f) }

        setUpContent { mutableState ->
            LaunchedEffect(Unit) {
                mutableState.value = 1f
            }
        }

        assertString(1f.toString())
        settings.get<Float>(key) shouldBe 1f
    }

    @Test
    fun shouldUpdateStringSettingOrNull() = runSettingsTest<Float?>(settings) {
        rememberMutableState = { rememberFloatSettingOrNull(key) }

        setUpContent { mutableState ->
            LaunchedEffect(Unit) {
                mutableState.value = 1f
            }
        }

        assertString(1f.toString())
        settings.get<Float>(key) shouldBe 1f
    }

    @Test
    fun shouldSyncTwoFloatSettings() = runSettingsTest<Float>(settings) {
        rememberMutableState = { rememberFloatSetting(key, 0f) }

        setUpContent {
            val secondStringSetting = rememberFloatSetting(key, 0f)
            LaunchedEffect(Unit) {
                secondStringSetting.value = 1f
            }
        }

        assertString(1f.toString())
        settings.get<Float>(key) shouldBe 1f
    }
}

