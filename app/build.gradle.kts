plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.aman"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.aman"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.androidsvg)
    implementation(libs.kxml2)
    implementation(libs.mapsforge.themes)
    implementation(libs.mapsforge.map.reader)
    implementation(libs.mapsforge.map.android)
    implementation(libs.mapsforge.map)
    implementation(libs.mapsforge.core)
    implementation(libs.espresso.core)
    implementation(libs.ext.junit)
    implementation(libs.junit)
    implementation(libs.legacy.support.v4)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}