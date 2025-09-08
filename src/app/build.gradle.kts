plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.2"
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.baulsupp.kolja:jcurses:0.9.5.3")
}

tasks.withType<JavaExec> {
    systemProperty("java.library.path", "/usr/java/packages/lib:/usr/lib/x86_64-linux-gnu")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest.attributes["Main-Class"] = "Main"
}

tasks.shadowJar {
    archiveBaseName.set("Rogue")
    archiveClassifier.set("")
    archiveVersion.set("1.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        // vendor.set(JvmVendorSpec.ANY) // Раскомментируйте, если нужно
    }
}

application {
    mainClass.set("Main")
    applicationDefaultJvmArgs = listOf(
        "-Djava.library.path=/usr/java/packages/lib:/usr/lib/x86_64-linux-gnu",
        "-Dorg.gradle.java.home=/usr/lib/jvm/java-21-openjdk-amd64"
    )

}


tasks.register("Rogue", Exec::class) {
    group = "application"
    dependsOn("shadowJar")
    println("Start Rogue Jar file")
    executable = "java"
    args("-jar", "build/libs/Rogue-1.0.jar")

}