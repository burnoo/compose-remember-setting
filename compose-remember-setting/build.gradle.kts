import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.dokka.gradle.DokkaTask
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
version = "1.0.0-SNAPSHOT"

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
            api(compose.runtime)
            implementation(compose.ui)
            implementation(libs.multiplatformSettings.makeObservable)
            implementation(libs.multiplatformSettings.noarg)
            implementation(libs.multiplatformSettings.test)
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

tasks.configureEach {
    if (name.contains("UnitTestKotlinAndroid")) {
        enabled = false
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

// Publishing configuration below
val currentProperties = rootProject.file("local.properties")
    .run { if (exists()) Properties().apply { load(reader()) } else properties }

val isRelease: Boolean
    get() = currentProperties["isRelease"]?.toString()?.toBoolean() == true

tasks.withType<DokkaTask>().configureEach {
    if (isRelease) {
        moduleVersion = moduleVersion.get().replace("-SNAPSHOT", "")
    }
}

extensions.findByType<PublishingExtension>()?.apply {
    repositories {
        maven {
            name = "sonatype"
            val repositoryId = currentProperties["sonatypeStagingRepositoryId"]?.toString()
            url = uri(
                if (repositoryId != null) {
                    "https://s01.oss.sonatype.org/service/local/staging/deployByRepositoryId/$repositoryId/"
                } else {
                    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                },
            )
            credentials {
                username = currentProperties["sonatypeUsername"]?.toString()
                password = currentProperties["sonatypePassword"]?.toString()
            }
        }
        maven {
            name = "sonatypeSnapshot"
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            credentials {
                username = currentProperties["sonatypeUsername"]?.toString()
                password = currentProperties["sonatypePassword"]?.toString()
            }
        }
    }
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
            if (isRelease) {
                version = version.replace("-SNAPSHOT", "")
            }

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

signing {
    val key = currentProperties["signingKey"]?.toString()?.replace("\\n", "\n")
    val password = currentProperties["signingPassword"]?.toString()
    useInMemoryPgpKeys(key, password)
    sign(publishing.publications)
}
