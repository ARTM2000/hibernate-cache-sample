plugins {
    java
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "me.artm2000"
version = "0.0.1-SNAPSHOT"
description = "hibernate-cache"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.5.5")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.5.5")
    implementation("org.springframework.boot:spring-boot-starter-jdbc:3.5.5")
    implementation("org.springframework.boot:spring-boot-starter-web:3.5.5")
    compileOnly("org.projectlombok:lombok:1.18.38")
    runtimeOnly("com.mysql:mysql-connector-j:8.4.0")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.13.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
