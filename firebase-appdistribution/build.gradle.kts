import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import suntrix.kmp.xcframework.configureCarthageFrameworks
import suntrix.kmp.xcframework.linkCarthageFrameworks

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

android {
    multiplatformLibrary()

    namespace = "suntrix.kmp.firebase.appdistribution"
}

kotlin {
    android()

    fun configureNativeTarget(): KotlinNativeTarget.() -> Unit = {
        val frameworks = firebaseCoreFrameworks().plus(
            listOf(
                "FirebaseAppDistribution"
            )
        )

        compilations.getByName("main") {
            cinterops.create("FirebaseAppDistribution") {
                configureCarthageFrameworks(target, rootDir, frameworks)
//                extraOpts = listOf("-compiler-option", "-DNS_FORMAT_ARGUMENT(A)=", "-verbose")
            }
        }

        binaries.all {
            linkCarthageFrameworks(rootDir, frameworks)
        }
    }

    iosWithArm64(configure = configureNativeTarget())
//    macosArm64(configure = configureNativeTarget())
//    macosX64(configure = configureNativeTarget())
//    tvos(configure = configureNativeTarget())
//    tvosSimulatorArm64(configure = configureNativeTarget())
//    watchos(configure = configureNativeTarget())
//    watchosSimulatorArm64(configure = configureNativeTarget())

    nativeSourceSets()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":extensions"))
                api(project(":firebase-core"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("com.google.firebase:firebase-appdistribution:16.0.0-beta08")
            }
        }
    }
}