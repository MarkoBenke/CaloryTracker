// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsKotlinJvm) apply false
    alias(libs.plugins.hiltAndroidGradlePlugin) apply false
}

buildscript {
    repositories {
        repositories {
            google()
            mavenCentral()
        }
        dependencies {
            classpath(libs.android.gradlePlugin)
            classpath(libs.kotlin.gradlePlugin)
        }
    }
}
