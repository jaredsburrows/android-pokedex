plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  kotlin("android") version libs.versions.kotlin.get() apply false
  kotlin("plugin.compose") version libs.versions.kotlin.get() apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.dagger) apply false
  alias(libs.plugins.ktlint) apply false
  alias(libs.plugins.versions)
//  alias(libs.plugins.oss.licenses.plugin)
}
