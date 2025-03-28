import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.android.hideapi"
    compileSdk = 35
    defaultConfig { minSdk = 26 }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    publishing { singleVariant("release") }
}

dependencies {
//    implementation(libs.androidx.core.ktx)
}
publishing {
    publications {
        register<MavenPublication>("release") {
            val today = SimpleDateFormat("yyyyMMdd").format(Date())
            groupId = "com.android.hideapi"
            artifactId = "hideapi"
            version = "1.0-${today}"

            afterEvaluate {
                from(components["release"])
            }
        }
    }

    repositories {
        mavenLocal()
        maven { url = uri("../../../SystemLib_repository") }
    }
}
//gradlew publish