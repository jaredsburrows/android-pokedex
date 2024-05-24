pluginManagement {
  repositories {
    mavenCentral()
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    maven("https://androidx.dev/storage/compose-compiler/repository/") {
      name = "compose-compiler"
      content {
        // this repository *only* contains compose-compiler artifacts
        includeGroup("androidx.compose.compiler")
      }
    }
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

  repositories {
    mavenCentral()
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    maven("https://androidx.dev/storage/compose-compiler/repository/") {
      name = "compose-compiler"
      content {
        // this repository *only* contains compose-compiler artifacts
        includeGroup("androidx.compose.compiler")
      }
    }
    gradlePluginPortal()
  }
}

plugins {
  `gradle-enterprise`
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
    publishAlways()
  }
}

rootProject.name = "android-pokedex"

include(":app-compose")
include(":test-resources")
