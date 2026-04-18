plugins {
    id("java")
}

group = "com.range"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Source: https://mvnrepository.com/artifact/org.eclipse.jgit/org.eclipse.jgit
    implementation("org.slf4j:slf4j-api:2.0.17")
    // Source: https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    implementation("ch.qos.logback:logback-classic:1.5.32")
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.6.0.202603022253-r")
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}