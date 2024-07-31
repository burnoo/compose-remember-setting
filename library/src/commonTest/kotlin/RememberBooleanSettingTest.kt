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
class RememberBooleanSettingTest {

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
    fun shouldRememberBooleanSetting() = runSettingsTest<Boolean>(settings) {
        rememberMutableState = { rememberBooleanSetting(key, false) }

        setUpContent()

        assertString(false.toString())
        settings[key] = true
        assertString(true.toString())
    }

    @Test
    fun shouldRememberBooleanSettingOrNull() = runSettingsTest<Boolean?>(settings) {
        rememberMutableState = { rememberBooleanSettingOrNull(key) }

        setUpContent()

        assertString("null")
        settings[key] = true
        assertString(true.toString())
    }

    @Test
    fun shouldUpdateStringSetting() = runSettingsTest<Boolean>(settings) {
        rememberMutableState = { rememberBooleanSetting(key, false) }

        setUpContent { mutableState ->
            LaunchedEffect(Unit) {
                mutableState.value = true
            }
        }

        assertString(true.toString())
        settings.get<Boolean>(key) shouldBe true
    }

    @Test
    fun shouldUpdateStringSettingOrNull() = runSettingsTest<Boolean?>(settings) {
        rememberMutableState = { rememberBooleanSettingOrNull(key) }

        setUpContent { mutableState ->
            LaunchedEffect(Unit) {
                mutableState.value = true
            }
        }

        assertString(true.toString())
        settings.get<Boolean>(key) shouldBe true
    }

    @Test
    fun shouldSyncTwoBooleanSettings() = runSettingsTest<Boolean>(settings) {
        rememberMutableState = { rememberBooleanSetting(key, false) }

        setUpContent {
            val secondStringSetting = rememberBooleanSetting(key, false)
            LaunchedEffect(Unit) {
                secondStringSetting.value = true
            }
        }

        assertString(true.toString())
        settings.get<Boolean>(key) shouldBe true
    }
}

