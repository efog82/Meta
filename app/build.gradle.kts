plugins {
id("com.android.application")
id("org.jetbrains.kotlin.android")
id("org.jetbrains.kotlin.kapt")
}
android {
namespace = "com.meta.assistant"
compileSdk = 34
defaultConfig { applicationId = "com.meta.assistant"; minSdk = 26; targetSdk = 34; versionCode = 1; versionName = "1.0" }
buildFeatures { viewBinding = true }
compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
kotlinOptions { jvmTarget = "17" }
}
dependencies {
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.appcompat:appcompat:1.6.1")
implementation("com.google.android.material:material:1.11.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
implementation("androidx.viewpager2:viewpager2:1.0.0")
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")
implementation("com.google.mediapipe:tasks-genai:0.10.14")
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
implementation("androidx.work:work-runtime-ktx:2.9.0")
}
