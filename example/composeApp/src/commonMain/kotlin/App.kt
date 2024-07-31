import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.burnoo.compose.remembersetting.rememberStringSetting
import dev.burnoo.compose.remembersetting.rememberStringSettingOrNull
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val KEY = "key"

@Composable
@Preview
fun App() {
    MaterialTheme {
        var text by rememberStringSetting(KEY, "Hello world!")
        val nullableText by rememberStringSettingOrNull(KEY)
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(text, onValueChange = { text = it })
            nullableText?.let { Text("Saved value: \"$it\"") }
        }
    }
}
