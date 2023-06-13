import net.minecraftforge.gradle.userdev.DependencyManagementExtension
import net.minecraftforge.gradle.userdev.UserDevExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.22"
}

buildscript {
    repositories {
        maven { url = uri("https://repo.siro256.dev/repository/maven-public/") }
    }

    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:5.1.69") {
            isChanging = true
        }
    }
}

apply(plugin = "net.minecraftforge.gradle")

group = "com.github.kotatsu-rtm.advancedrails"
version = "1.5"

repositories {
    maven { url = uri("https://repo.siro256.dev/repository/maven-public/") }
    maven { url = uri("https://cursemaven.com") }
}

dependencies {
    val fgDepManager = project.extensions[DependencyManagementExtension.EXTENSION_NAME] as DependencyManagementExtension

    api(kotlin("stdlib"))

    add("minecraft", "net.minecraftforge:forge:1.12.2-14.23.5.2860")

    implementation(fgDepManager.deobf("curse.maven:ngtlib-288989:3873392"))
    implementation(fgDepManager.deobf("curse.maven:realtrainmod-288988:3873403"))
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

configure<UserDevExtension> {
    mappings("snapshot", "20171003-1.12")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            allWarningsAsErrors = true
        }
    }

    withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(
            configurations.api.get().copy()
                .apply { isCanBeResolved = true }
                .map { if (it.isDirectory) it else zipTree(it) }
        )
    }
}
