plugins {
    alias(libs.plugins.android.app)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.hilt)
    alias(libs.plugins.lsplugin.apksign)
}

apksign {
    storeFileProperty = "KEYSTORE_FILE"
    storePasswordProperty = "KEYSTORE_PASSWORD"
    keyAliasProperty = "KEY_ALIAS"
    keyPasswordProperty = "KEY_PASSWORD"
}

android {
    namespace = "com.looker.kenko"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.looker.kenko"
        minSdk = 26
        targetSdk = 35
        // 1.2.0 -> 102000
        // 1.12.2 -> 112020
        // 1.12.10 -> 112100
        versionCode = 102000
        versionName = "1.2.0"

        testInstrumentationRunner = "com.looker.kenko.KenkoTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        room {
            schemaDirectory("$projectDir/schemas")
        }
    }

    dependenciesInfo {
        includeInApk = false
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-Xcontext-receivers"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    sourceSets {
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }


    composeCompiler {
        enableStrongSkippingMode = true
        metricsDestination = file("$projectDir/reports/metrics")
        reportsDestination = file("$projectDir/reports")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    kotlin("stdlib")
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.core.ktx)

    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))

    implementation(libs.bundles.lifecycle)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.graphics.shapes)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.debug)

    implementation(libs.bundles.coil)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.datastore)

    implementation(libs.bundles.room)
    testImplementation(libs.androidx.room.test)
    ksp(libs.androidx.room.compiler)

    // Rebugger
    implementation(libs.rebugger)

    kspTest(libs.hilt.test)
    testImplementation(libs.bundles.test)

    kspAndroidTest(libs.hilt.test)
    androidTestImplementation(libs.bundles.instrumented.test)
}

fun DependencyHandlerScope.kotlin(name: String): Any = kotlin(name, libs.versions.kotlin.get())
