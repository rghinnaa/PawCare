object Dependencies {

    object Plugins {
        const val application = "com.android.application"
        const val kotlinJetbrains = "org.jetbrains.kotlin.android"
        const val kotlinAndroid = "kotlin-android"
        const val kotlinKapt = "kotlin-kapt"
        const val navSafeArgs = "androidx.navigation.safeargs.kotlin"
        const val hilt = "dagger.hilt.android.plugin"
        const val kotlinParcelize = "kotlin-parcelize"
        const val googleService = "com.google.gms.google-services"
    }

    object Logging {
        const val timber = "com.jakewharton.timber:timber:${Version.timberVersion}"
    }

    object Testing {
        const val jUnit = "junit:junit:${Version.jUnit}"
        const val jUnitTest = "androidx.test.ext:junit:${Version.testJUnit}"
        const val espresso = "androidx.test.espresso:espresso-core:${Version.espresso}"
        const val mockk = "io.mockk:mockk:${Version.mockk}"
        const val coroutineTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutineTest}"
        const val archTest = "androidx.arch.core:core-testing:${Version.archTest}"
        const val truth = "com.google.truth:truth:${Version.truth}"
        const val hiltTesting = "com.google.dagger:hilt-android-testing:${Version.Classpath.hiltVersion}"
    }

    object ClassPath {
        const val gradle = "com.android.tools.build:gradle:${Version.Classpath.gradleVersion}"
        const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.Classpath.kotlin}"
        const val navArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Version.Classpath.navVersion}"
        const val hiltAndroid = "com.google.dagger:hilt-android-gradle-plugin:${Version.Classpath.hiltVersion}"
        const val googleService = "com.google.gms:google-services:${Version.Classpath.googleService}"
    }

    object UI {
        const val activityKtx = "androidx.activity:activity-ktx:${Version.activityVersion}"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:${Version.fragmentVersion}"
        const val appCompat = "androidx.appcompat:appcompat:${Version.appCompatVersion}"
        const val materialDesign =
            "com.google.android.material:material:${Version.materialDesignVersion}"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Version.constraintVersion}"
        const val intuitSdp = "com.intuit.sdp:sdp-android:${Version.intuitVersion}"
        const val intuitSsp = "com.intuit.ssp:ssp-android:${Version.intuitVersion}"
        const val sweetAlert = "com.github.f0ris.sweetalert:library:${Version.sweetAlert}"
        const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Version.swipeRefresh}"
        val uiDependencies = mutableListOf(
            activityKtx,
            fragmentKtx,
            appCompat,
            materialDesign,
            constraintLayout,
            intuitSdp,
            intuitSsp,
            sweetAlert,
            swipeRefresh
        )
    }

    object Jetpack {
        const val viewModelKtx =  "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycleVersion}"
        const val viewModelRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycleVersion}"
        const val lifeCycle = "androidx.lifecycle:lifecycle-common-java8:${Version.lifecycleVersion}"
        const val liveDataKtx =  "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycleVersion}"
        const val paging = "androidx.paging:paging-runtime:${Version.pagingVersion}"
        const val navigation = "androidx.navigation:navigation-fragment-ktx:${Version.navVersion}"
        const val navigationRuntime = "androidx.navigation:navigation-runtime:${Version.navVersion}"
        const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Version.navVersion}"
        const val workRuntime = "androidx.work:work-runtime:${Version.workRuntimeVersion}"
        const val dataStore = "androidx.datastore:datastore-preferences:${Version.dataStoreVersion}"

        val jetpackComponentDependencies = mutableListOf(
            viewModelKtx, viewModelRuntime, lifeCycle, liveDataKtx, paging,
            navigation, navigationRuntime, navigationUi, workRuntime, dataStore
        )
    }

    object Firebase {
        const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
        const val firebaseBom = "com.google.firebase:firebase-bom:${Version.firebaseBom}"
    }

    object Network {
        const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofitVersion}"
        const val gson = "com.squareup.retrofit2:converter-gson:${Version.retrofitVersion}"
        const val okHttp = "com.squareup.okhttp3:logging-interceptor:${Version.okHttpVersion}"
        const val volley = "com.android.volley:volley:${Version.volleyVersion}"

        val networkDependencies = mutableListOf(
            retrofit, gson, okHttp, volley
        )
    }

    object Kotlin {
        const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlinVersion}"

        object Coroutines {
            const val coroutinesCore =
                "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutinesVersion}"
            const val coroutinesAndroid =
                "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutinesVersion}"
            const val coroutinesPlayService =
                "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Version.coroutinesVersion}"

            val coroutinesDependencies =
                mutableListOf(coroutinesCore, coroutinesAndroid, coroutinesPlayService)
        }
    }

    object Injection {
        const val dagger = "com.google.dagger:hilt-android:${Version.hiltVersion}"
        const val fragmentNavigationInject =
            "androidx.hilt:hilt-navigation-fragment:${Version.hiltViewModelVersion}"

        val injectionDependencies = mutableListOf(
            dagger, fragmentNavigationInject
        )
    }

    object Kapt {
        const val glide = "com.github.bumptech.glide:compiler:${Version.glide}"
        const val androidCompiler = "com.google.dagger:hilt-android-compiler:${Version.hiltVersion}"

        val kaptImp = mutableListOf(glide, androidCompiler)
    }

    const val coreKtx = "androidx.core:core-ktx:${Version.coreKtx}"
    const val playCoreKtx = "com.google.android.play:core-ktx:${Version.playCoreKtxVersion}"
    const val glide = "com.github.bumptech.glide:glide:${Version.glide}"

    object Repositories {
        const val jitpackUrl = "https://jitpack.io"
    }
}