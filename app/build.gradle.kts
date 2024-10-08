plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.weatherui"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.weatherui"
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
}


dependencies {
    implementation("io.github.ParkSangGwon:tedpermission-normal:3.4.2")
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.poi)
    implementation(libs.poi.ooxml)
    implementation(libs.play.services.location)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.mpandroidchart)
    implementation(libs.viewpager2)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.core.splashscreen)
    implementation(libs.lottie)
    implementation(libs.fragment)
}
