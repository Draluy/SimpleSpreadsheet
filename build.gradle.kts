import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    `maven-publish`
    signing
    application
}

group = "fr.raluy.simplespreadsheet"
version = "1.0"


java {
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    val apachePoiVersion = "5.2.2"

    implementation("org.apache.poi:poi:$apachePoiVersion")
    implementation("org.apache.poi:poi-ooxml:$apachePoiVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
    implementation("com.opencsv:opencsv:5.6")

    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation(kotlin("test"))
}

publishing {

    publications {
        create<MavenPublication>("mavenJava") {
            signing {
                sign(configurations.archives.get())
                sign(publishing.publications["mavenJava"])
            }
            from(components["java"])
            pom {
                name.set("Simple Spreadsheet")
                packaging = "jar"

                description.set("A simple spreasdheet parser.")
                url.set("https://github.com/Draluy/SimpleSpreadsheet")
                licenses {
                    license {
                        name.set("GNU General Public License, version 2")
                        url.set("https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html")
                    }
                }
                developers {
                    developer {
                        id.set("draluy")
                        name.set("David Raluy")
                        email.set("david@raluy.fr")
                    }
                }
                scm {
                    connection.set("git@github.com:Draluy/SimpleSpreadsheet.git")
                    developerConnection.set("git@github.com:Draluy/SimpleSpreadsheet.git")
                    url.set("https://github.com/Draluy/SimpleSpreadsheet")
                }
            }
        }
    }

    repositories {
        maven {
            name = "myRepo"
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            val ossrhUsername: String by project
            val ossrhPassword: String by project

            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }

}



tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}




