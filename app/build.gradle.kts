plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android") // Hilt plugin
}

android {
    namespace = "com.dynamite.proyectox"
    compileSdk = 36 // ACTUALIZADO

    defaultConfig {
        applicationId = "com.dynamite.proyectox"
        minSdk = 24
        targetSdk = 36 // ACTUALIZADO
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17 // ACTUALIZADO
        targetCompatibility = JavaVersion.VERSION_17 // ACTUALIZADO
    }
    kotlinOptions {
        jvmTarget = "17" // ACTUALIZADO
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get() // Asegúrate que composeCompiler esté en tu libs.versions.toml
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui) 
    implementation(libs.androidx.compose.ui.graphics) 
    implementation(libs.androidx.compose.ui.tooling.preview) 
    implementation(libs.androidx.compose.material3)
    
    // Iconos de Material Design - Nueva forma de alias
    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose) 

    // Hilt Dependencies
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0") 

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4) 
    debugImplementation(libs.androidx.compose.ui.tooling) 
    debugImplementation(libs.androidx.compose.ui.test.manifest) 
}

// Allow references to generated code (Necesario para Kapt con Hilt)
kapt {
    correctErrorTypes = true
}
