plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
}

android {
    namespace = "com.example.financebudgetapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.financebudgetapp"
        minSdk = 24
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
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.filament.android)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.compiler) // Add this line (use the same version as room-runtime)
    // Room KTX for Coroutines (if you're using coroutines for database operations)
    implementation(libs.androidx.room.ktx) // Add this line (use the same version as room-runtime)

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit for networking
    implementation(libs.retrofit)
    // Converter for JSON parsing (Gson)
    implementation(libs.converter.gson)
    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.core) // Use the latest version
    implementation(libs.kotlinx.coroutines.android) // Use the latest version

    // ViewModel and LiveData/StateFlow
    implementation(libs.androidx.lifecycle.viewmodel.compose) // Use the latest version
    implementation(libs.androidx.lifecycle.runtime.compose) // Use the latest version

    // Compose Material 3 for Dropdown and UI elements
    implementation(libs.androidx.material3.v121)
    implementation(libs.material3) // Use the latest version
    implementation(libs.ui.tooling.preview) // For previewing Composable

}