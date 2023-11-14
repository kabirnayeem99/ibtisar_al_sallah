plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization").version("1.9.20")
}

kotlin {
    androidTarget()

    jvm("desktop")

    listOf(
        iosX64(), iosArm64(), iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val ktorVersion = "2.3.5"
        val voyagerVersion = "1.0.0-rc10"

        val commonMain by getting {
            dependencies {
                implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
                implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")

                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                val koinVersion = "3.5.0"
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("cafe.adriel.voyager:voyager-koin:$voyagerVersion")
//                implementation("io.insert-koin:koin-ktor:$koinVersion")

                implementation("co.touchlab:kermit:2.0.2")
//                val napierVersion = "2.9.1"
//                implementation ("io.github.aakira:napier:$napierVersion")

                implementation("media.kamel:kamel-image:0.8.3")

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class) implementation(
                    compose.components.resources
                )
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.activity:activity-compose:1.8.0")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.12.0")
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                val koinAndroidVersion = "3.5.0"
                implementation("io.insert-koin:koin-android:$koinAndroidVersion")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "io.github.kabirnayeem99.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}
