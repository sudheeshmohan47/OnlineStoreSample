import java.util.Locale

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.kotlincompose)
    alias(libs.plugins.detekt)
    jacoco
}

android {
    namespace = "com.sample.onlinestore.productsmodule"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "com.sample.productsmodule.HiltTestRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {

    implementation(project(":designsystem"))
    implementation(project(":commonmodule"))
    implementation(project(":cartmodule"))
    implementation(project(":wishlistmodule"))
    implementation(project(":categoriesmodule"))

    // kotlin core
    implementation(libs.androidx.core.ktx)
    // compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.compose.ui.ui.graphics)
    implementation(libs.androidx.compose.ui.ui.tooling.preview)
    implementation(libs.androidx.compose.material3.material3)
    implementation(libs.androidx.lifecycle.runtime.compose.v270)
    // coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlin.stdlib.v1924)
    // immutable collections
    implementation(libs.immutable.collections)
    // image loading
    implementation(libs.coil)
    implementation(libs.coil.compose)
    // Compose Animation
    implementation(libs.androidx.animation)
    // Navigation
    implementation(libs.androidx.navigation.compose.v280alpha08)
    implementation(libs.androidx.hilt.navigation.compose)
    // DataStore Preferences
    implementation(libs.androidx.datastore.preferences)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // hilt
    implementation(libs.google.dagger.hilt)
    implementation(libs.play.services.analytics.impl)
    testImplementation(libs.androidx.core.testing)
    ksp(libs.google.dagger.hilt.compiler)
    // timber
    implementation(libs.timber)

    // Detekt compose rules
    detektPlugins(libs.detekt)

    testImplementation (libs.mockk)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation (libs.mockk.android)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(libs.androidx.core.testing)

    testImplementation (libs.hilt.android.testing)
    kspTest (libs.hilt.android.compiler.v2511)
    androidTestImplementation (libs.hilt.android.testing)
    kspAndroidTest (libs.google.dagger.hilt.compiler)
}

val exclusions = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*"
)

tasks.withType(Test::class) {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

afterEvaluate {
    tasks.register<JacocoReport>("JacocoCodeCoverage") {
        dependsOn(listOf("testDebugUnitTest"))
        group = "Reporting"
        description = "Execute ui and unit tests, generate and combine Jacoco coverage report"
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
        sourceDirectories.setFrom(layout.projectDirectory.dir("src/main"))
        classDirectories.setFrom(files(
            fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
                exclude(exclusions)
            },
            fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
                exclude(exclusions)
            }
        ))
        executionData.setFrom(files(
            fileTree(layout.buildDirectory) { include(listOf("**/*.exec", "**/*.ec")) }
        ))
    }
}