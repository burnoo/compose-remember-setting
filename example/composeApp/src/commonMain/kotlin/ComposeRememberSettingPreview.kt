import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import dev.burnoo.compose.remembersetting.ComposeRememberSettingConfig
import dev.burnoo.compose.remembersetting.LocalComposeRememberSettingConfig

@Composable
fun ComposeRememberSettingPreview(vararg keyValues: Pair<String, Any>, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalComposeRememberSettingConfig provides ComposeRememberSettingConfig(
            mutableMap = mutableMapOf(*keyValues),
        ),
        content = content,
    )
}
