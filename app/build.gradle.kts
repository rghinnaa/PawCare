plugins {
    id(Dependencies.Plugins.application)
    id(Dependencies.Plugins.kotlinParcelize)
    id(Dependencies.Plugins.kotlinJetbrains)
    id(Dependencies.Plugins.kotlinAndroid)
    id(Dependencies.Plugins.kotlinKapt)
    id(Dependencies.Plugins.navSafeArgs)
    id(Dependencies.Plugins.hilt)
    id(Dependencies.Plugins.googleService)
}

android {
    compileSdk = 33
    buildToolsVersion = "30.0.3"

    ndkVersion = "21.4.7075529"

    defaultConfig {
        applicationId = "com.example.pawcare"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        minSdk = 23
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.glide)
    implementation(Dependencies.playCoreKtx)
    implementation(Dependencies.Kotlin.kotlin)

    Dependencies.Kotlin.Coroutines.coroutinesDependencies.forEach { dependency ->
        implementation(dependency)
    }

    Dependencies.UI.uiDependencies.forEach { dependency ->
        implementation(dependency)
    }

    Dependencies.Jetpack.jetpackComponentDependencies.forEach { dependency ->
        implementation(dependency)
    }

    Dependencies.Network.networkDependencies.forEach { dependency ->
        implementation(dependency)
    }

    Dependencies.Injection.injectionDependencies.forEach { dependency ->
        implementation(dependency)
    }

    Dependencies.Kapt.kaptImp.forEach { kaptImp ->
        kapt(kaptImp)
    }

    implementation(Dependencies.Logging.timber)
    implementation(platform(Dependencies.Firebase.firebaseBom))
    implementation(Dependencies.Firebase.firebaseAnalytics)

    testImplementation(Dependencies.Testing.jUnit)
    androidTestImplementation(Dependencies.Testing.jUnitTest)
    androidTestImplementation(Dependencies.Testing.espresso)
}