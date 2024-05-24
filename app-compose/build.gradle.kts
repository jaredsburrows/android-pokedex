import org.gradle.api.JavaVersion.VERSION_11
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11

buildscript {
  repositories {
    mavenCentral()
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
  }

  dependencies {
    classpath("com.google.android.gms:oss-licenses-plugin:0.10.6")
  }
}

plugins {
  alias(libs.plugins.android.application)
  kotlin("android")
  kotlin("plugin.compose")
  alias(libs.plugins.ksp)
  alias(libs.plugins.dagger)
  alias(libs.plugins.ktlint)
}

apply(plugin = "com.google.android.gms.oss-licenses-plugin")

android {
  namespace = "com.burrowsapps.pokedex"
  testNamespace = "com.burrowsapps.pokedex.test"
  compileSdk = libs.versions.sdk.compile.get().toInt()

  defaultConfig {
    applicationId = "com.burrowsapps.pokedex"
    versionCode = 1
    versionName = "1.0"
    minSdk = libs.versions.sdk.min.get().toInt()
    targetSdk = libs.versions.sdk.target.get().toInt()

    testApplicationId = "com.burrowsapps.pokedex.test"
    testInstrumentationRunner = "com.burrowsapps.pokedex.test.CustomTestRunner"
    testInstrumentationRunnerArguments +=
      mapOf(
        "disableAnalytics" to "true",
      )

    resourceConfigurations += setOf("en")
    vectorDrawables.useSupportLibrary = true
  }

  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = VERSION_11
    targetCompatibility = VERSION_11
  }

  kotlinOptions {
    jvmTarget = VERSION_11.toString()
  }

  kotlin {
    compilerOptions {
      jvmTarget = JVM_11
    }
  }

  buildFeatures {
    buildConfig = true
    compose = true
  }

  lint {
    abortOnError = true
    checkAllWarnings = true
    warningsAsErrors = true
    checkTestSources = true
    checkDependencies = true
    checkReleaseBuilds = false
    lintConfig = file("${project.rootDir}/config/lint/lint.xml")
    textReport = true
    sarifReport = true
  }

  signingConfigs {
    getByName("debug") {
      storeFile = file("${project.rootDir}/config/signing/debug.keystore")
      storePassword = libs.versions.debug.password.get()
      keyAlias = libs.versions.debug.alias.get()
      keyPassword = libs.versions.debug.password.get()
    }
  }

  buildTypes {
    debug {
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-dev"
      signingConfig = signingConfigs.getByName("debug")
    }

    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles +=
        listOf(
          getDefaultProguardFile("proguard-android-optimize.txt"),
          file("${project.rootDir}/config/proguard/proguard-rules.pro"),
        )
      signingConfig = signingConfigs.getByName("debug")
    }
  }

  testOptions {
    unitTests.apply {
      isReturnDefaultValues = true
      isIncludeAndroidResources = true
    }
    animationsDisabled = true
    execution = "ANDROIDX_TEST_ORCHESTRATOR"
  }

//  packaging.resources {  }

//  packaging {
//    resources.excludes += listOf(
//      "**/*.kotlin_module",
//      "**/*.version",
//      "**/kotlin/**",
//      "**/*.txt",
//      "**/*.xml",
//      "**/*.properties",
//    )
//  }

  dependenciesInfo {
    includeInApk = false
    includeInBundle = false
  }
}

hilt {
  enableAggregatingTask = true
//  enableExperimentalClasspathAggregation = true
}

ksp {
  arg("dagger.formatGeneratedSource", "disabled")
  arg("dagger.fastInit", "enabled")
  arg("dagger.experimentalDaggerErrorMessages", "enabled")
}

dependencies {
  implementation("com.google.android.gms:play-services-oss-licenses:17.0.1")

  // JDK 11 libs
  coreLibraryDesugaring(libs.android.desugar)

  // Kotlin
  implementation(platform(libs.kotlin.bom))

  // KotlinX
  implementation(platform(libs.kotlinx.coroutines.bom))
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.kotlinx.coroutinesjdk8)
  testImplementation(libs.kotlinx.coroutines.test)

  // Dagger / Dependency Injection
  implementation(libs.google.hilt.android)
  implementation(libs.androidx.hilt.compose)
  ksp(libs.google.hilt.compiler)
  kspTest(libs.google.hilt.compiler)
  kspAndroidTest(libs.google.hilt.compiler)
  compileOnly(libs.glassfish.javax.annotation)
  compileOnly(libs.javax.annotation.jsr250)
  compileOnly(libs.google.findbugs.jsr305)
  testImplementation(libs.google.hilt.testing)
  androidTestImplementation(libs.google.hilt.testing)

  // AndroidX
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.core)
  implementation(libs.androidx.startup)

  // AndroidX Lifecycle
  implementation(libs.androidx.lifecycle.viewmodel)
  implementation(libs.androidx.paging.runtime)

  // AndroidX UI
  implementation(libs.google.material)

  // AndroidX Jetpack Compose
  implementation(platform(libs.androidx.compose.bom))
  debugImplementation(libs.androidx.compose.uitoolingpreview)
  debugImplementation(libs.androidx.compose.uimanifest)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.android)
  implementation(libs.androidx.compose.material.icons.extended.android)
  implementation(libs.androidx.compose.paging)
  implementation(libs.androidx.compose.navigation)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.uitooling)
  implementation(libs.google.accompanist.drawablepainter)
  implementation(libs.google.accompanist.swiperefresh)
  androidTestImplementation(libs.androidx.compose.junit)

  // Espresso Interop
  androidTestImplementation(libs.androidx.espresso.core)

  // Glide
  implementation(libs.landscapist.animation)
  implementation(libs.landscapist.glide)
  implementation(libs.landscapist.palette)
  implementation(libs.bumptech.glide)
  implementation(libs.bumptech.glide.okhttp)
  ksp(libs.bumptech.glide.ksp)

  // OkIO
  implementation(libs.squareup.okio)

  // OkHTTP
  implementation(platform(libs.squareup.okhttp.bom))
  implementation(libs.squareup.okhttp)
  implementation(libs.squareup.okhttp.logging)
  testImplementation(libs.squareup.okhttp.mockwebserver)
  androidTestImplementation(libs.squareup.okhttp.mockwebserver)

  // Retrofit
  implementation(libs.squareup.moshi)
  implementation(libs.squareup.moshi.adapters)
  ksp(libs.squareup.moshi.kotlin)
  implementation(libs.squareup.retrofit)
  implementation(libs.squareup.retrofit.moshi)
  implementation(libs.squareup.retrofit.scalars)

  // Leakcanary
  debugImplementation(libs.squareup.leakcanary)
  androidTestImplementation(libs.squareup.leakcanary.instrumentation)

  // Other
  implementation(libs.jakewharton.timber)

  // Unit Tests
  testImplementation(project(":test-resources"))
  testImplementation(libs.androidx.test.annotation)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.androidx.test.junit)
  testImplementation(libs.google.truth)
  testImplementation(libs.junit)
  testImplementation(libs.mockito.inline)
  testImplementation(libs.mockito.kotlin)
  testImplementation(libs.robolectric)

  // Android Tests
  androidTestUtil(libs.androidx.test.orchestrator)
  androidTestImplementation(project(":test-resources"))
  androidTestImplementation(libs.androidx.test.annotation)
  androidTestImplementation(libs.androidx.test.core)
  androidTestImplementation(libs.androidx.test.junit)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.androidx.test.rules)
  androidTestImplementation(libs.google.truth)
  androidTestImplementation(libs.junit)
  androidTestImplementation(libs.robolectric.annotations)
}
