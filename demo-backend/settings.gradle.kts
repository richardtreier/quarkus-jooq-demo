pluginManagement {
  val quarkusPluginVersion: String by settings
  val quarkusPluginId: String by settings
  val kotlinVersion: String by settings

  repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
  }

  plugins {
    id(quarkusPluginId) version quarkusPluginVersion
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
  }
}

rootProject.name = "demo-backend"

include(
  "demo-db",
  "demo-quarkus"
)
