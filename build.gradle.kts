
plugins {
	java
	`maven-publish`
}

version = "1.2"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(11))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.google.guava:guava:31.1-jre")
	implementation("commons-io:commons-io:2.11.0")

	implementation("org.jetbrains:annotations:23.0.0")

	implementation("org.slf4j:slf4j-api:2.0.3")

	testImplementation("junit:junit:4.13.2")
	testImplementation("org.slf4j:slf4j-simple:2.0.3")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = "com.github.taskeren"
			artifactId = "config"
			version = "${project.version}"

			from(components["java"])
		}
	}

	repositories {
		maven {
			url = if(!"$version".endsWith("SNAPSHOT")) {
				uri("https://play.elytra.cn:11443/releases")
			} else {
				uri("https://play.elytra.cn:11443/snapshots")
			}

			credentials(PasswordCredentials::class)
		}
	}
}