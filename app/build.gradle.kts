import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)     //hilt
    alias(libs.plugins.hilt)    //hilt
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.hrr_android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.hrr_android"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties().apply {
            load(project.rootProject.file("local.properties").inputStream())
        }
//        val baseUrl = properties.getProperty("BASE_URL") ?: ""
//        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        val baseUrl = properties.getProperty("BASE_URL") ?: ""
        if (baseUrl.isEmpty()) {
            throw GradleException("BASE_URL is not defined in local.properties")
        }

        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

        // Kakao App Key 설정
        val kakaoAppKey = properties.getProperty("KAKAO_APP_KEY") ?: ""
        buildConfigField("String", "KAKAO_APP_KEY", "\"$kakaoAppKey\"")
        // AndroidManifest.xml에서 사용할 키 추가
        manifestPlaceholders["kakaoAppKey"] = kakaoAppKey
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
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.circularprogressbar)
    implementation(libs.androidx.viewpager2)
    implementation(libs.gson)

    // Retrofit2 의존성
    implementation(libs.retrofit)
    implementation(libs.converter.gson) // JSON 변환용

    // OkHttp3 의존성
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor) // 네트워크 로그 확인용

    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Fragment KTX (activityViewModels() 사용 가능)
    implementation(libs.androidx.fragment.ktx)

    // LiveData KTX (LiveData 사용 최적화)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Glide
    implementation(libs.glide)
    kapt(libs.glideCompiler)

    // EncryptedSharedPreferences 및 MasterKey 사용을 위한 AndroidX Security Crypto 의존성 추가
    implementation (libs.androidx.security.crypto.ktx.v110alpha06)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    
    // Kakao SDK
    implementation (libs.v2.user)

    // Bottom Navigation Component
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
}
