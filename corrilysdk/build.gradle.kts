plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  kotlin("plugin.serialization") version "1.9.21"
}

android {
  namespace = "com.corrily.corrilysdk"
  compileSdk = 34

  defaultConfig {
    minSdk = 21

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.1"
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

  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.9.0")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
  implementation("androidx.datastore:datastore-preferences:1.0.0")

  implementation(platform("androidx.compose:compose-bom:2023.08.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.animation:animation")
  implementation("androidx.compose.material3:material3")

  val lifecycle_version = "2.6.2"
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
  implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")
  implementation("androidx.compose.runtime:runtime-livedata:1.5.4")
  implementation("io.coil-kt:coil-compose:2.5.0")

  val billing_version = "6.1.0"
  implementation("com.android.billingclient:billing-ktx:$billing_version")

  debugImplementation("androidx.compose.ui:ui-tooling")
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}