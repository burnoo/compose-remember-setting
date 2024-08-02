package dev.burnoo.compose.remembersetting.internal

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AsMutableStateTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun shouldGetAsMutableState() = runTest {
        val testScope = CoroutineScope(Dispatchers.Unconfined)
        val stateFlow = MutableStateFlow(0)

        val mutableState = stateFlow.asMutableState(testScope, setValue = { stateFlow.value = it })

        stateFlow.value = 1
        mutableState.value shouldBe 1

        mutableState.value = 2
        stateFlow.value shouldBe 2
    }

    @Test
    fun shouldCancelsSettingValue() = runTest {
        val testDispatcher = StandardTestDispatcher()
        val testScope = CoroutineScope(testDispatcher)
        val stateFlow = MutableStateFlow(0)

        val mutableState = stateFlow.asMutableState(testScope, setValue = {
            delay(1.seconds)
            stateFlow.value = it
        })

        stateFlow.value = 1
        testDispatcher.scheduler.advanceUntilIdle()
        mutableState.value shouldBe 1

        mutableState.value = 2
        stateFlow.value shouldBe 1
        mutableState.value = 3
        testDispatcher.scheduler.advanceUntilIdle()
        stateFlow.value shouldBe 3
    }
}
