plugins {
    id("java")


    id("org.graalvm.buildtools.native") version "1.0.0"


}

group = "com.range"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.5.32")
    implementation("org.eclipse.jgit:org.eclipse.jgit.ssh.jsch:7.6.0.202603022253-r")
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.6.0.202603022253-r")
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}
graalvmNative {
    toolchainDetection.set(true)
}
graalvmNative {
    binaries {
        named("main") {
            mainClass.set("Main")

            imageName.set("auto-commiter")
        }
    }
}
graalvmNative {
    binaries {
        named("main") {
            buildArgs.add("-H:+AddAllCharsets")
            buildArgs.add("--add-exports=java.base/sun.security.x509=ALL-UNNAMED")
            buildArgs.add("--add-exports=java.base/sun.security.util=ALL-UNNAMED")

            buildArgs.add("--initialize-at-build-time=sun.security.x509.X509CertImpl")
            buildArgs.add("--initialize-at-build-time=sun.security.x509.X509CertInfo")

            buildArgs.add("--enable-https")
            buildArgs.add("--enable-http")
        }
    }
}
tasks.test {
    useJUnitPlatform()
}