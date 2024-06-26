plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
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
    buildFeatures {
        viewBinding = true
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
    implementation(libs.play.services.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.ui.database)
    implementation(libs.androidx.recyclerview)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.location)
    implementation(libs.volley)
    implementation(libs.picasso)
    implementation(libs.circleimageview)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    

}