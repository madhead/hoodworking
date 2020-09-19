import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.lang.System.getenv as env

plugins {
    kotlin("jvm").version("1.4.10")
    application
    id("org.liquibase.gradle").version("2.0.4")
    kotlin("plugin.serialization").version("1.4.10")
}

repositories {
    jcenter()
}

sourceSets {
    create("dbTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val dbTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}
val dbTestRuntimeOnly: Configuration by configurations.getting {
    extendsFrom(configurations.testRuntimeOnly.get())
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:1.4.0"))
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.13.3"))

    implementation("io.ktor:ktor-server-netty")
    implementation("org.koin:koin-ktor:2.1.6")
    implementation("com.github.insanusmokrassar:TelegramBotAPI:0.28.2")
    implementation("com.github.rodionmoiseev.c10n:c10n-core:1.3")
    implementation("org.postgresql:postgresql:42.2.16")
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl")

    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")

    testRuntimeOnly(platform("org.junit:junit-bom:5.7.0"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    dbTestImplementation("org.postgresql:postgresql:42.2.16")

    liquibaseRuntime("org.liquibase:liquibase-core:4.0.0")
    liquibaseRuntime("org.yaml:snakeyaml:1.27")
    liquibaseRuntime("org.postgresql:postgresql:42.2.16")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

liquibase {
    activities {
        register("hoodworking") {
            this.arguments = mapOf(
                    "url" to "jdbc:postgresql://${env("POSTGRES_HOST")}:${env("POSTGRES_PORT")}/${env("POSTGRES_DB")}",
                    "username" to env("POSTGRES_USER"),
                    "password" to env("POSTGRES_PASSWORD"),
                    "driver" to "org.postgresql.Driver",
                    "changeLogFile" to "src/main/liquibase/changelog.yml"
            )
        }
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "13"
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
        }
    }

    val dbTest by creating(Test::class) {
        val dbTest by sourceSets

        group = "verification"
        testClassesDirs = dbTest.output.classesDirs
        classpath = dbTest.runtimeClasspath
        shouldRunAfter("test")
    }
}
