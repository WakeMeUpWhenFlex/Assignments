plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.assignment1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.assignment1"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.compose.ui:ui:1.5.0") // 与 Kotlin 1.9.0 兼容的 Compose 版本
    implementation("androidx.compose.material:material:1.5.0") // 与 Kotlin 1.9.0 兼容的 Compose 版本
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0") // 与 Kotlin 1.9.0 兼容的 Compose 版本
    implementation("androidx.compose.runtime:runtime-livedata:1.5.0") // 与 Kotlin 1.9.0 兼容的 Compose 版本
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.0") // 与 Kotlin 1.9.0 兼容的 Compose 版本
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
