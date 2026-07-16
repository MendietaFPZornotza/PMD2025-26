plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Agrega el plugin kapt
    kotlin("kapt")
}

android {
    namespace = "com.example.proiekto_denda"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.proiekto_denda"
        minSdk = 24
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Dependencias adicionales
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.room:room-runtime:2.4.1")
    kapt("androidx.room:room-compiler:2.4.1") // kapt con comillas dobles
}
