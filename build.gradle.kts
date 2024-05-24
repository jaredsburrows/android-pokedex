import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.util.Locale

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  kotlin("android") version (libs.versions.kotlin.get()) apply false
  kotlin("jvm") version (libs.versions.kotlin.get()) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.dagger) apply false
  alias(libs.plugins.ktlint) apply false
  alias(libs.plugins.versions)
//  alias(libs.plugins.oss.licenses.plugin)
}

allprojects {
  tasks.withType(Wrapper::class.java).configureEach {
    distributionType = Wrapper.DistributionType.ALL
  }

  tasks.withType(DependencyUpdatesTask::class.java).configureEach {
    fun isNonStable(version: String): Boolean {
      val stableKeyword =
        listOf("RELEASE", "FINAL", "GA").any { version.uppercase(Locale.getDefault()).contains(it) }
      val regex = "^v?[0-9]+(\\.[0-9]+)*$".toRegex()
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
