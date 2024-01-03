import java.util.Properties

plugins {
	id("com.android.application")
	kotlin("android")
	id("com.google.devtools.ksp")
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "com.eva.pexelsapp"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.eva.pexelsapp"
		minSdk = 26
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

		rootProject.file("pexels.properties").apply file@{
			if (!exists()) return@file
			val properties = Properties()
			inputStream().use { stream ->
				properties.load(stream)
				// API_KEY
				val apiKey = properties.getProperty("API_KEY")
				buildConfigField(type = "String", name = "API_KEY", value = "\"$apiKey\"")
				// BASE_URL
				val baseUrl = properties.getProperty("BASE_URL")
				buildConfigField(type = "String", name = "BASE_URL", value = "\"$baseUrl\"")
			}
		}
	}


	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		viewBinding = true
		buildConfig = true
	}
}

dependencies {

	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.appcompat:appcompat:1.6.1")
	//material and constraintlayout
	implementation("com.google.android.material:material:1.11.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	// navigation
	implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
	implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
	//retrofit
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
	//moshi
	implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
	ksp("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")
	//paging
	implementation("androidx.paging:paging-runtime:3.2.1")
	//coil
	implementation("io.coil-kt:coil:2.5.0")
	// dependency injection
	implementation("com.google.dagger:hilt-android:2.50")
	ksp("com.google.dagger:hilt-android-compiler:2.50")
	//tests
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}