plugins {
    kotlin("jvm") version "2.1.20"
    id("io.qameta.allure") version "2.12.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.3"))
    testImplementation(platform("io.qameta.allure:allure-bom:2.29.0"))

    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("io.rest-assured:rest-assured:5.5.0")
    testImplementation("com.google.code.gson:gson:2.11.0") // JSON object mapper for REST-assured .as<T>()
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("io.qameta.allure:allure-junit5")
    testImplementation("io.qameta.allure:allure-rest-assured")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

allure {
    version.set("2.29.0")
}

tasks.test {
    useJUnitPlatform()
    providers.gradleProperty("baseUri").orElse(providers.systemProperty("baseUri")).orNull
        ?.let { systemProperty("baseUri", it) }
    testLogging { events("passed", "skipped", "failed") }
}
