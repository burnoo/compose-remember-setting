# compose-remember-setting 🧠⚙️
[![Maven Central](https://img.shields.io/maven-central/v/dev.burnoo/compose-remember-setting)](https://search.maven.org/search?q=dev.burnoo.compose-remember-setting) [![javadoc](https://javadoc.io/badge2/dev.burnoo/compose-remember-setting/javadoc.svg?label=dokka&logo=)](https://javadoc.io/doc/dev.burnoo/compose-remember-setting)

Compose Multiplatform library for remembering state persistently based on [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings). It's a persistent version of `remember { mutableStateOf(x) }`.

Demo: https://burnoo.github.io/compose-remember-setting/

## Installation
The library is distributed through Maven Central. To use it, add the following dependency to your module `build.gradle.kts`:
```kotlin
dependencies {
    implementation("dev.burnoo:compose-remember-setting:1.0.0")
}
```

## Usage
To store mutable state in `@Composable` when the app is running, you usually use `remember { mutableStateOf(x) }`. This library provides the same functionality but supports data persistence, saving, and restoring data using Multiplatform Settings.
```kotlin
val count: MutableState<Int> = rememberIntSetting(key = "intKey", defaultValue = 0)
var text: String? by rememberStringSettingOrNull(key = "stringKey")
val (checked, setChecked) = rememberBooleanSetting(key = "booleanKey", defaultValue = false)
```
Full example can be found [here](example/composeApp/src/commonMain/kotlin/App.kt).

## Configuration
You can configure the library using the local composition mechanism to pass `ComposeRememberSettingConfig`, which stores `ObservableSettings` and `CoroutineDispatcher` for use by the children.
```kotlin
@Composable
fun WithSettings(content: @Composable () -> Unit)() {
    CompositionLocalProvider(
        LocalComposeRememberSettingConfig provides ComposeRememberSettingConfig(
            observableSettings = CustomSettings().makeObservable(),
            flowSettingsDispatcher = Dispatchers.Unconfined
        ),
        content = content
    )
}
```
#### Using Config with `@Preview`
<details>
<summary>Code</summary>

```kotlin
@Composable
fun ComposeRememberSettingPreview(
    vararg keyValues: Pair<String, Any>,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalComposeRememberSettingConfig provides ComposeRememberSettingConfig(
            mutableMap = mutableMapOf(*keyValues),
        ),
        content = content,
    )
}

@Preview
@Composable
fun CounterPreview() {
    ComposeRememberSettingPreview("intKey" to 21) {
        var counter: Int by rememberIntSetting(key = "intKey", defaultValue = 0)
        Button(onClick = { counter++ }) { Text(counter.toString()) }
    }
}
```
</details>

#### Using Config in tests

<details>
<summary>Code</summary>

```kotlin
@Composable
private fun WithTestComposeSettings(
    observableSettings: ObservableSettings,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalComposeRememberSettingConfig provides ComposeRememberSettingConfig(
            observableSettings = observableSettings,
            flowSettingsDispatcher = Dispatchers.Unconfined,
        ),
        content = content,
    )
}

@OptIn(ExperimentalTestApi::class)
class ComposeTest {

    private val settings = MapSettings(mutableMapOf("intKey" to 21))

    @Test
    fun counterTest() = runComposeUiTest {
        setContent {
            WithTestComposeSettings(settings) {
                var counter: Int by rememberIntSetting(key = "intKey", defaultValue = 0)
            }
        }
    }
}
```
</details>

## Supported types
- `String` - `rememberStringSetting` / `rememberStringSettingOrNull`
- `Int` - `rememberIntSetting`, `rememberIntSettingOrNull`
- `Long` - `rememberLongSetting`, `rememberLongSettingOrNull`
- `Boolean` - `rememberBooleanSetting`, `rememberBooleanSettingOrNull`
- `Float` - `rememberFloatSetting`, `rememberFloatSettingOrNull`
- `Double` - `rememberDoubleSetting`, `rememberDoubleSettingOrNull`

## Why
You might ask why someone would need to use this library. The data layer should not be used directly on the UI and should be handled in the repository.

This is valid for any more complicated logic. However, when there is a need to store a very simple state of the UI persistently, the library becomes very handy, reducing coupling.

Example: Imagine a user wants the checkbox state to be remembered after the app is restarted. With this library, the code is very simple: `var checked by rememberBooleanSetting(key = "booleanKey", defaultValue = false)`.

Implementing this in a clean codish manner is much more complicated. We would need a `ViewModel` (with passing lambda and state through all the composables), a `LayoutStateRepository` (assuming we don't want to use `Settings` in the `ViewModel` either, as we use a `Repository` abstraction over data sources), a `LayoutStateLocalDataSource`, and maybe even `GetCheckboxStateUseCase` and `SetCheckboxStateUseCase` if the `ViewModel` doesn’t depend on :data directly.

## License
```
Copyright 2024 Bruno Wieczorek

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
