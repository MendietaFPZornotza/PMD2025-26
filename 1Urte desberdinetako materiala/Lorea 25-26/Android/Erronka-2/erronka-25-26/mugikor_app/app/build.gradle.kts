import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.erronka_proba"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.erronka_proba"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProps = Properties().apply {
            val localPropertiesFile = rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                load(FileInputStream(localPropertiesFile))
            }
        }
        val tcpHost = localProps.getProperty("TCP_HOST")
        val tcpPort = localProps.getProperty("TCP_PORT")

        val ftpsHost = localProps.getProperty("FTPS_HOST")
        val ftpsPort = localProps.getProperty("FTPS_PORT")
        val ftpsUser = localProps.getProperty("FTPS_USER")
        val ftpsPass = localProps.getProperty("FTPS_PASS")


        buildConfigField("String", "TCP_HOST", "\"$tcpHost\"")
        buildConfigField("int", "TCP_PORT", tcpPort.toString())


        buildConfigField("String", "FTPS_HOST", "\"$ftpsHost\"")
        buildConfigField("int", "FTPS_PORT", ftpsPort.toString())
        buildConfigField("String", "FTPS_USER", "\"$ftpsUser\"")
        buildConfigField("String", "FTPS_PASS", "\"$ftpsPass\"")


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
        buildConfig = true
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
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
    implementation("commons-net:commons-net:3.12.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

}
