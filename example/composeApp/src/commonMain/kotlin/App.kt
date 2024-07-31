import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.burnoo.compose.remembersetting.rememberBooleanSetting
import dev.burnoo.compose.remembersetting.rememberIntSetting
import dev.burnoo.compose.remembersetting.rememberStringSetting
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var text by rememberStringSetting("stringKey", "Hello world!")
        val (checked, setChecked) = rememberBooleanSetting("booleanKey", false)
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("The below states will be saved persistently:")
            TextField(text, onValueChange = { text = it })
            Checkbox(checked, setChecked)
            Counter()
        }
    }
}

@Composable
private fun Counter() {
    var counter by rememberIntSetting("intKey", 0)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = { counter-- }) { Text("-") }
        Text(
            counter.toString(),
            style = MaterialTheme.typography.h3.copy(textAlign = TextAlign.Center),
            modifier = Modifier.defaultMinSize(minWidth = 120.dp)
        )
        Button(onClick = { counter++ }) { Text("+") }
    }
}
