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
class RememberLongSettingTest {

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
    fun shouldRememberLongSetting() = runSettingsTest<Long>(settings) {
        rememberMutableState = { rememberLongSetting(key, 0L) }

        setUpContent()

        assertString(0L.toString())
        settings[key] = 1L
        assertString(1L.toString())
    }

    @Test
    fun shouldRememberLongSettingOrNull() = runSettingsTest<Long?>(settings) {
        rememberMutableState = { rememberLongSettingOrNull(key) }

        setUpContent()

        assertString("null")
        settings[key] = 1L
        assertString(1L.toString())
    }

    @Test
    fun shouldUpdateStringSetting() = runSettingsTest<Long>(settings) {
        rememberMutableState = { rememberLongSetting(key, 0L) }

        setUpContent { mutableState ->
            LaunchedEffect(Unit) {
                mutableState.value = 1L
            }
        }

        assertString(1L.toString())
        settings.get<Long>(key) shouldBe 1L
    }

    @Test
    fun shouldUpdateStringSettingOrNull() = runSettingsTest<Long?>(settings) {
        rememberMutableState = { rememberLongSettingOrNull(key) }

        setUpContent { mutableState ->
            LaunchedEffect(Unit) {
                mutableState.value = 1L
            }
        }

        assertString(1L.toString())
        settings.get<Long>(key) shouldBe 1L
    }

    @Test
    fun shouldSyncTwoLongSettings() = runSettingsTest<Long>(settings) {
        rememberMutableState = { rememberLongSetting(key, 0L) }

        setUpContent {
            val secondStringSetting = rememberLongSetting(key, 0L)
            LaunchedEffect(Unit) {
                secondStringSetting.value = 1L
            }
        }

        assertString(1L.toString())
        settings.get<Long>(key) shouldBe 1L
    }
}

