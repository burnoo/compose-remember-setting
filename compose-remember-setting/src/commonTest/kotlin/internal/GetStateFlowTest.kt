package dev.burnoo.compose.remembersetting.internal

import app.cash.turbine.test
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalSettingsApi::class)
class GetStateFlowTest {

    private val testScope = CoroutineScope(Dispatchers.Unconfined)
    private val flowSettings = MapSettings().toFlowSettings(dispatcher = Dispatchers.Unconfined)
    private val key = "key"

    @Test
    fun shouldGetStateFlow() = runTest {
        val stateFlow = flowSettings.getStateFlow(testScope, key, initialValue = 0)

        stateFlow.test {
            awaitItem() shouldBe 0
            flowSettings.putInt(key, 1996)
            awaitItem() shouldBe 1996
        }
    }

    @Test
    fun shouldGetNullableStateFlow() = runTest {
        val stateFlow = flowSettings.getStateFlow<Int?>(testScope, key, initialValue = null)

        stateFlow.test {
            awaitItem() shouldBe null
            flowSettings.putInt(key, 1996)
            awaitItem() shouldBe 1996
        }
    }
}
