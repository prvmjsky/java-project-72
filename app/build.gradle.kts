plugins {
    application
    checkstyle
    jacoco
    id("org.sonarqube") version "6.1.0.5360"
    id("com.github.ben-manes.versions") version "0.52.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

application {
    mainClass.set("hexlet.code.App")
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val javalinVersion = "6.7.0"
    val jteVersion = "3.2.1"
    var lombokVersion = "1.18.38"

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.squareup.okhttp3:mockwebserver3:5.1.0")
    testImplementation("org.mockito:mockito-core:5.18.0")

    implementation("io.javalin:javalin-bundle:$javalinVersion")
    implementation("io.javalin:javalin-rendering:$javalinVersion")
    implementation("gg.jte:jte:$jteVersion")
    implementation("gg.jte:jte-watcher:$jteVersion")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("com.h2database:h2:2.3.232")
    implementation("com.zaxxer:HikariCP:6.3.0")
    implementation("org.postgresql:postgresql:42.7.7")
    implementation("com.konghq:unirest-java-core:4.4.7")
    implementation("org.jsoup:jsoup:1.21.1")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
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