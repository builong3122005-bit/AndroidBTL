plugins {
    alias(libs.plugins.android.application)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

android {
    namespace = "com.example.baitaplon"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.baitaplon"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    sourceSets {
        getByName("main") {
            res.srcDirs(
                "src/main/res/layouts/trang_chao",
                "src/main/res/layouts/trang_dangnhap",
                "src/main/res/layouts/trang_dangky",
                "src/main/res/layouts/trang_chuyendoi",
                "src/main/res/layouts/trang_thitruong",
                "src/main/res/layouts/trang_bieudo",
                "src/main/res/layouts/trang_tintuc",
                "src/main/res/layouts/trang_caidat",
                "src/main/res/layouts/trang_hoso",
                "src/main/res"
            )
        }
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("com.opencsv:opencsv:5.5.2") {
        exclude(group = "commons-logging", module = "commons-logging")
    }
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}