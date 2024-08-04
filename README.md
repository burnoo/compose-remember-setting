# compose-remember-setting

Compose Multiplatform library for remembering state persistently (based on Multiplatform Settings).

WIP 🚧

`build.gradle.kts`:
```kotlin
dependencies {
    implementation("dev.burnoo:compose-remember-setting:0.0.0")
}
```

`settings.gradle.kts` (necessary for having [unreleased version](https://github.com/burnoo/multiplatform-settings) of Multiplatform Settings 1.2.0, that is published to [my maven](https://github.com/burnoo/maven) and used by this library) 
```kotlin
dependencyResolutionManagement {
    repositories {
        maven(url = "https://pkgs.dev.azure.com/burnoo/maven/_packaging/public/maven/v1") {
            content {
                includeVersionByRegex(".*", ".*", ".*-beap[0-9]+")
            }
        }
    }
}
