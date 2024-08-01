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
class RememberIntSettingTest {

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
    fun shouldRememberIntSetting() = runSettingsTest<Int>(settings) {
        rememberMutableState = { rememberIntSetting(key, 0) }

        setUpContent()

        assertString("0")
        settings[key] = 1
        assertString("1")
    }

    @Test
    fun shouldRememberIntSettingOrNull() = runSettingsTest<Int?>(settings) {
        rememberMutableState = { rememberIntSettingOrNull(key) }

        setUpContent()

        assertString("null")
        settings[key] = 1
        assertString("1")
    }

    @Test
    fun shouldUpdateStringSetting() = runSettingsTest<Int>(settings) {
        rememberMutableState = { rememberIntSetting(key, 0) }

        setUpContent { mutableState ->
            LaunchedEffect(Unit) {
                mutableState.value = 1
            }
        }

        assertString("1")
        settings.get<Int>(key) shouldBe 1
    }

    @Test
    fun shouldUpdateStringSettingOrNull() = runSettingsTest<Int?>(settings) {
        rememberMutableState = { rememberIntSettingOrNull(key) }

        setUpContent { mutableState ->
            LaunchedEffect(Unit) {
                mutableState.value = 1
            }
        }

        assertString("1")
        settings.get<Int>(key) shouldBe 1
    }

    @Test
    fun shouldSyncTwoIntSettings() = runSettingsTest<Int>(settings) {
        rememberMutableState = { rememberIntSetting(key, 0) }

        setUpContent {
            val secondStringSetting = rememberIntSetting(key, 0)
            LaunchedEffect(Unit) {
                secondStringSetting.value = 1
            }
        }

        assertString("1")
        settings.get<Int>(key) shouldBe 1
    }
}
