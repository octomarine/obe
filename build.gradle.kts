plugins {
    kotlin("jvm") version "1.6.21"
}

group = "me.myeolchi"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("me.dolphin2410:tap-api:5.0.0")
    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}