@file:Suppress("UnstableApiUsage")

rootProject.name = "compose-remember-setting"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven(url = "https://pkgs.dev.azure.com/burnoo/maven/_packaging/public/maven/v1") {
            content {
                includeVersionByRegex(".*", ".*", ".*-beap[0-9]+")
            }
        }
    }
}

include(":library")
include(":example:composeApp")
