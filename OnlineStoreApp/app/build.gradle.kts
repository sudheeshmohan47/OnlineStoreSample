plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.kotlincompose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.roomPlugin)
}

android {
    namespace = "com.sample.onlinestore"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sample.onlinestore"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        buildConfig = true
        compose = true
    }
    room {
        schemaDirectory ("$projectDir/schemas")
    }
}

dependencies {

    implementation(project(":designsystem"))
    implementation(project(":commonmodule"))
    implementation(project(":datastoragemodule"))
    implementation(project(":cartmodule"))
    implementation(project(":wishlistmodule"))
    implementation(project(":authenticationmodule"))
    implementation(project(":productsmodule"))
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

    // room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // hilt
    implementation(libs.google.dagger.hilt)
    ksp(libs.google.dagger.hilt.compiler)
    // timber
    implementation(libs.timber)

    // Detekt compose rules
    detektPlugins(libs.detekt)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}