plugins {
    id("java")
    checkstyle
    jacoco
    id("org.sonarqube") version "6.1.0.5360"
    id("com.github.ben-manes.versions") version "0.52.0"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
    reports {
        dependsOn(tasks.test)
        xml.required.set(true)
    }
}

sonar {
    properties {
        property("sonar.projectKey", "prvmjsky_java-project-72")
        property("sonar.organization", "prvmjsky")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}