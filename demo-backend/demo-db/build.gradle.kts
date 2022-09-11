import org.flywaydb.gradle.task.FlywayMigrateTask
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainer

plugins {
  kotlin("jvm")
  id("org.flywaydb.flyway") version "9.3.0"
  id("nu.studer.jooq") version "7.1.1"
  `java-library`
}

val jooqDbType = "org.jooq.meta.postgres.PostgresDatabase"
val jdbcDriver = "org.postgresql.Driver"
val postgresContainer = "postgres:14-alpine"

val migrationsDir = "src/main/resources/db/migration"
val jooqTargetPackage = "de.richardtreier.demo.dao.schema"
val jooqTargetSourceRoot = "build/generated/jooq"

val jooqTargetDir = jooqTargetSourceRoot + "/" + jooqTargetPackage.replace(".", "/")
val flywayMigration = configurations.create("flywayMigration")

buildscript {
  dependencies {
    classpath("org.testcontainers:postgresql:1.17.5")
  }
}

dependencies {
  api("org.jooq:jooq:3.16.4")
  jooqGenerator("org.postgresql:postgresql:42.5.0")
  flywayMigration("org.postgresql:postgresql:42.5.0")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
  kotlinOptions.javaParameters = true
}

sourceSets {
  main {
    java {
      srcDirs.add(File(jooqTargetSourceRoot))
    }
  }
}

var container: JdbcDatabaseContainer<*>? = null
tasks.register("startTestcontainer") {
  doLast {
    container = PostgreSQLContainer<Nothing>(postgresContainer)
    container!!.start()
    gradle.buildFinished {
      container?.stop()
    }
  }
}

flyway {
  driver = jdbcDriver
  schemas = arrayOf("public")

  cleanDisabled = false
  baselineOnMigrate = true
  locations = arrayOf("filesystem:${migrationsDir}")
  configurations = arrayOf("flywayMigration")
}

tasks.withType<FlywayMigrateTask> {
  dependsOn.add("startTestcontainer")
  doFirst {
    require(this is FlywayMigrateTask)
    require(container?.isRunning ?: false) { "DB Testcontainer should be running!" }
    url = container!!.jdbcUrl
    user = container!!.username
    password = container!!.password
  }
}

jooq {
  configurations {
    create("main") {
      jooqConfiguration.apply {
        generator.apply {
          database.apply {
            name = jooqDbType
            excludes = "flyway_schema_history"
            inputSchema = flyway.schemas[0]
          }
          generate.apply {
            isRecords = true
          }
          target.apply {
            packageName = jooqTargetPackage
            directory = jooqTargetDir
          }
        }
      }
    }
  }
}

tasks.withType<nu.studer.gradle.jooq.JooqGenerate> {
  dependsOn.add("flywayMigrate")
  inputs.files(fileTree(migrationsDir))
    .withPropertyName("migrations")
    .withPathSensitivity(PathSensitivity.RELATIVE)
  allInputsDeclared.set(true)
  outputs.cacheIf { true }
  doFirst {
    require(this is nu.studer.gradle.jooq.JooqGenerate)
    require(container?.isRunning ?: false) { "DB Testcontainer should be running!" }

    val jooqConfiguration = nu.studer.gradle.jooq.JooqGenerate::class.java.getDeclaredField("jooqConfiguration")
      .also { it.isAccessible = true }.get(this) as org.jooq.meta.jaxb.Configuration

    jooqConfiguration.jdbc.apply {
      url = container!!.jdbcUrl
      user = container!!.username
      password = container!!.password
    }
  }
  doLast {
    container?.stop()
  }
}
