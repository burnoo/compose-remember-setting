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
class RememberDoubleSettingTest {

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
    fun shouldRememberDoubleSetting() = runSettingsTest<Double>(settings) {
        rememberMutableState = { rememberDoubleSetting(key, .0) }

        setUpContent()

        assertString(.0.toString())
        settings[key] = 1.0
        assertString(1.0.toString())
    }

    @Test
    fun shouldRememberDoubleSettingOrNull() = runSettingsTest<Double?>(settings) {
        rememberMutableState = { rememberDoubleSettingOrNull(key) }

        setUpContent()

        assertString("null")
        settings[key] = 1.0
        assertString(1.0.toString())
    }

    @Test
    fun shouldUpdateStringSetting() = runSettingsTest<Double>(settings) {
        rememberMutableState = { rememberDoubleSetting(key, .0) }

        setUpContent { mutableState ->
            LaunchedEffect(Unit) {
                mutableState.value = 1.0
            }
        }

        assertString(1.0.toString())
        settings.get<Double>(key) shouldBe 1.0
    }

    @Test
    fun shouldUpdateStringSettingOrNull() = runSettingsTest<Double?>(settings) {
        rememberMutableState = { rememberDoubleSettingOrNull(key) }

        setUpContent { mutableState ->
            LaunchedEffect(Unit) {
                mutableState.value = 1.0
            }
        }

        assertString(1.0.toString())
        settings.get<Double>(key) shouldBe 1.0
    }

    @Test
    fun shouldSyncTwoDoubleSettings() = runSettingsTest<Double>(settings) {
        rememberMutableState = { rememberDoubleSetting(key, .0) }

        setUpContent {
            val secondStringSetting = rememberDoubleSetting(key, .0)
            LaunchedEffect(Unit) {
                secondStringSetting.value = 1.0
            }
        }

        assertString(1.0.toString())
        settings.get<Double>(key) shouldBe 1.0
    }
}

