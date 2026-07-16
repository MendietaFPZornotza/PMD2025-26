plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.praktika13retrofit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.praktika13retrofit"
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
    buildFeatures{
        viewBinding= true
    }
}

dependencies {

    //implementation(libs.androidx.core.ktx)
    //implementation(libs.androidx.appcompat)
    //implementation(libs.material)
    //implementation(libs.androidx.activity)
    //implementation(libs.androidx.constraintlayout)
    //testImplementation(libs.junit)
    //androidTestImplementation(libs.androidx.junit)
    //androidTestImplementation(libs.androidx.espresso.core)

    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.8.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation ("junit:junit:4.13.2'")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.10.0") // Última versión de Retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.10.0")   // Última versión del convertidor Gson
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")   // Última versión de OkHttp
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")   // Última versión del interceptor
}