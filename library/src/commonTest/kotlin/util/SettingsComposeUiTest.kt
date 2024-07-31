package dev.burnoo.compose.remembersetting.util

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import com.russhwolf.settings.ObservableSettings
import dev.burnoo.compose.remembersetting.LocalFlowSettingsDispatcher
import dev.burnoo.compose.remembersetting.LocalObservableSettings
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalTestApi::class)
class SettingsComposeUiTest<T>(
    private val composeUiTest: ComposeUiTest,
    private val settings: ObservableSettings,
) {

    lateinit var rememberMutableState: @Composable () -> MutableState<T>

    fun setUpContent(extraContent: @Composable ((mutableState: MutableState<T>) -> Unit)? = null) {
        composeUiTest.setContent {
            CompositionLocalTestWrapper(settings) {
                val mutableState = rememberMutableState()
                BasicText(mutableState.value.toString(), modifier = Modifier.testTag(TAG))
                extraContent?.invoke(mutableState)
            }
        }
    }

    fun assertString(expected: String) {
        composeUiTest.onNodeWithTag(TAG).assertTextEquals(expected)
    }

    private companion object {
        const val TAG = "tag"
    }
}

@Composable
private fun CompositionLocalTestWrapper(observableSettings: ObservableSettings, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalObservableSettings provides observableSettings) {
        CompositionLocalProvider(LocalFlowSettingsDispatcher provides Dispatchers.Unconfined) {
            content()
        }
    }
}
