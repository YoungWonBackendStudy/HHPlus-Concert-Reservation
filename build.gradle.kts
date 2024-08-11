plugins {
	java
	id("org.springframework.boot") version "3.2.7"
	id("io.spring.dependency-management") version "1.1.5"
}

group = "io.hhplus"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
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
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.kafka:spring-kafka")
	testImplementation("org.springframework.kafka:spring-kafka-test")

	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.rest-assured:rest-assured")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	//swagger-ui
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

	//redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis:3.3.2")
	implementation("org.redisson:redisson-spring-boot-starter:3.33.0")
	implementation("io.lettuce:lettuce-core:6.3.2.RELEASE")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
