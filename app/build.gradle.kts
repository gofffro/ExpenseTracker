import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) load(file.inputStream())
}

android {
    namespace = "com.example.expensetracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.expensetracker"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "APPMETRICA_API_KEY",
            "\"${localProperties["appmetrica_api_key"]}\""
        )
        buildConfigField(
            "String",
            "YANDEX_MAPS_API_KEY",
            "\"${localProperties["yandex_maps_api_key"] ?: localProperties["appmetrica_api_key"]}\""
        )
        manifestPlaceholders["YANDEX_CLIENT_ID"] = localProperties["yandex_client_id"]?.toString() ?: ""
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
        buildConfig = true
    }
    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":feature:list"))
    implementation(project(":feature:add"))
    implementation(project(":feature:detail"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:about"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.appmetrica.analytics)
    implementation(libs.yandex.authsdk)
    implementation(libs.vk.sdk.core)
    implementation(libs.vk.sdk.api)
    implementation(libs.androidx.security.crypto)
    implementation(libs.yandex.maps.mobile)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.config)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.crashlytics)

    testImplementation(libs.junit)
    testImplementation(libs.konsist)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
