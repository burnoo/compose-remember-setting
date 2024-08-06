package dev.burnoo.compose.remembersetting

import App
import ComposeRememberSettingPreview
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppAndroidPreview() {
    ComposeRememberSettingPreview("intKey" to 21, "booleanKey" to true) {
        App()
    }
}
