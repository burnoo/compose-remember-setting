import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dokka)
    `maven-publish`
    signing
}

group = "dev.burnoo"
version = "0.1.0"

kotlin {
    androidTarget {
        publishLibraryVariants("release", "debug")

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }

    jvm("desktop")

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val desktopTest by getting
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core)
            api(libs.multiplatformSettings.core)
            api(libs.multiplatformSettings.coroutines)
            implementation(compose.runtime)
            implementation(libs.multiplatformSettings.makeObservable)
            implementation(libs.multiplatformSettings.noarg)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.multiplatformSettings.test)
            implementation(libs.kotest.assertions)
            implementation(libs.turbine)
            implementation(compose.foundation)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }
        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

android {
    namespace = "dev.burnoo.compose.remembersetting"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    androidTestImplementation(libs.androidx.compose.test.junit4)
    debugImplementation(libs.androidx.compose.test.manifest)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    dependsOn("dokkaHtml")
    from("${layout.buildDirectory}/dokka/html")
}

extensions.findByType<PublishingExtension>()?.apply {
    publications.withType<MavenPublication>().configureEach {
        val publication = this
        val dokkaJar = project.tasks.register("${publication.name}DokkaJar", Jar::class) {
            archiveClassifier.set("javadoc")
            from(tasks.named("dokkaHtml"))
            archiveBaseName.set("${archiveBaseName.get()}-${publication.name}")
        }
        artifact(dokkaJar)
        pom {
            name = project.name
            description = "Compose Multiplatform library for remembering state persistently (based on Multiplatform Settings)"
            url = "https://github.com/burnoo/compose-remember-setting"

            licenses {
                license {
                    name = "The Apache License, Version 2.0"
                    url = "https://www.apache.org/licenses/LICENSE-2.0"
                }
            }

            developers {
                developer {
                    id = "burnoo"
                    name = "Bruno Wieczorek"
                    url = "https://burnoo.dev"
                    email = "bruno.wieczorek@gmail.com"
                }
            }

            scm {
                connection = "scm:git:git@github.com:burnoo/compose-remember-setting.git"
                url = "https://github.com/burnoo/compose-remember-setting"
                tag = "HEAD"
            }
        }
    }
}

val currentProperties = rootProject.file("local.properties")
    .run { if (exists()) Properties().apply { load(reader()) } else properties }

signing {
    val key = currentProperties["signing.key"]?.toString()?.replace("\\n", "\n")
    val password = currentProperties["signing.password"]?.toString()
    useInMemoryPgpKeys(key, password)
    sign(publishing.publications)
}
