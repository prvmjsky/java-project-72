plugins {
    application
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

    implementation("io.javalin:javalin:6.6.0")
    implementation("org.slf4j:slf4j-simple:2.0.7")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation("com.h2database:h2:2.2.220")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.7.7")
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