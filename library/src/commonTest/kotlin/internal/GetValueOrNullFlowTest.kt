package dev.burnoo.compose.remembersetting.internal

import app.cash.turbine.test
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalSettingsApi::class)
class GetValueOrNullFlowTest {

    private val flowSettings = MapSettings().toFlowSettings(dispatcher = Dispatchers.Unconfined)
    private val key = "key"

    @Test
    fun shouldReturnValueOrNullIntFlow() = runTest {
        flowSettings.getValueOrNullFlow<Int>(key).test {
            awaitItem() shouldBe null
            flowSettings.putInt(key, 1996)
            awaitItem() shouldBe 1996
        }
    }

    @Test
    fun shouldReturnValueOrNullLongFlow() = runTest {
        flowSettings.getValueOrNullFlow<Long>(key).test {
            awaitItem() shouldBe null
            flowSettings.putLong(key, 1996L)
            awaitItem() shouldBe 1996L
        }
    }

    @Test
    fun shouldReturnValueOrNullStringFlow() = runTest {
        flowSettings.getValueOrNullFlow<String>(key).test {
            awaitItem() shouldBe null
            flowSettings.putString(key, "Bruno")
            awaitItem() shouldBe "Bruno"
        }
    }

    @Test
    fun shouldReturnValueOrNullFloatFlow() = runTest {
        flowSettings.getValueOrNullFlow<Float>(key).test {
            awaitItem() shouldBe null
            flowSettings.putFloat(key, 1996f)
            awaitItem() shouldBe 1996f
        }
    }

    @Test
    fun shouldReturnValueOrNullDoubleFlow() = runTest {
        flowSettings.getValueOrNullFlow<Double>(key).test {
            awaitItem() shouldBe null
            flowSettings.putDouble(key, 1996.0)
            awaitItem() shouldBe 1996.0
        }
    }

    @Test
    fun shouldReturnValueOrNullBooleanFlow() = runTest {
        flowSettings.getValueOrNullFlow<Boolean>(key).test {
            awaitItem() shouldBe null
            flowSettings.putBoolean(key, true)
            awaitItem() shouldBe true
        }
    }

    @Test
    fun shouldThrowForInvalidType() = runTest {
        shouldThrow<IllegalArgumentException> {
            flowSettings.getValueOrNullFlow<GetValueOrNullFlowTest>(key)
        }
    }
}
