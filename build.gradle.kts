
plugins {
	java
	`maven-publish`
}

version = "1.1"

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.google.guava:guava:27.0.1-jre")
	implementation("commons-io:commons-io:2.4")
	testImplementation("junit:junit:4.12")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = "com.github.taskeren"
			artifactId = "tconfig"
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