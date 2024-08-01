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
import dev.burnoo.compose.remembersetting.ComposeRememberSettingConfig
import dev.burnoo.compose.remembersetting.LocalComposeRememberSettingConfig
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalTestApi::class)
class SettingsComposeUiTest<T>(
    private val composeUiTest: ComposeUiTest,
    private val settings: ObservableSettings,
) {

    lateinit var rememberMutableState: @Composable () -> MutableState<T>

    @Composable
    fun TestText(text: String) {
        BasicText(text, modifier = Modifier.testTag(TAG))
    }

    fun setUpContent(extraContent: @Composable ((mutableState: MutableState<T>) -> Unit)? = null) {
        composeUiTest.setContent {
            CompositionLocalTestConfigWrapper(settings) {
                val mutableState = rememberMutableState()
                TestText(mutableState.value.toString())
                extraContent?.invoke(mutableState)
            }
        }
    }

    fun overrideContent(content: @Composable () -> Unit) {
        composeUiTest.setContent {
            CompositionLocalTestConfigWrapper(settings) {
                content()
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
private fun CompositionLocalTestConfigWrapper(observableSettings: ObservableSettings, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalComposeRememberSettingConfig provides ComposeRememberSettingConfig(
            observableSettings = observableSettings,
            flowSettingsDispatcher = Dispatchers.Unconfined
        ),
        content = content
    )
}
