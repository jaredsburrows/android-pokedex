import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.util.Locale

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  kotlin("android") version libs.versions.kotlin.get() apply false
  kotlin("plugin.compose") version libs.versions.kotlin.get() apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.dagger) apply false
  alias(libs.plugins.ktlint) apply false
  alias(libs.plugins.versions)
}

allprojects {
  configurations.configureEach {
    resolutionStrategy {
      preferProjectModules()

      enableDependencyVerification()
    }
  }

  tasks.withType<DependencyUpdatesTask>().configureEach {
    fun isNonStable(version: String): Boolean {
      val stableKeyword =
        listOf("RELEASE", "FINAL", "GA").any { version.uppercase(Locale.getDefault()).contains(it) }
      val regex = "^[0-9,.v-]+(-r)?$".toRegex()
      val isStable = stableKeyword || regex.matches(version)
      return isStable.not()
    }

    resolutionStrategy {
      componentSelection {
        all {
          when (candidate.group) {
            "androidx.compose.compiler", "com.android.application", "org.jetbrains.kotlin" -> {}
            else -> {
              if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                reject("Release candidate")
              }
            }
          }
        }
      }
    }
  }
}
