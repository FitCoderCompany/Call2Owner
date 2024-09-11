plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)

    id("kotlin-kapt")
//    id("com.google.dagger.hilt.android")
//    id("com.google.gms.google-services")
}

android {
    lint {
        baseline = file("lint-baseline.xml")
    }


    bundle {
        language {
            enableSplit = false
        }
    }

    namespace = "com.call2owner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.call2owner"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
//            storeFile = file("D:\\FitCoder\\Wltpe-New\\info.jks")
             storeFile = file("/Users/mac/Desktop/Workspace/Wltpe/wltpe-info/wltpe.jks")
            storePassword = "wltpe78"
            keyPassword = "wltpe78"
            keyAlias = "upload"
        }
    }


    buildTypes {
        debug {
            android.buildFeatures.buildConfig= true
            isMinifyEnabled =false
            isShrinkResources= false
            isDebuggable =true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        release {
            android.buildFeatures.buildConfig= true
            isMinifyEnabled =true
            isShrinkResources= true
            isDebuggable =false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }



    applicationVariants.all {
        outputs.all {o->
            project.ext.set("appName", "call2owner-app")
            o.outputFile.name.replace("app-", "${project.ext["appName"]}-")
            true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

//    packagingOptions {
//        resources {
//            excludes += listOf(
//                "AndroidManifest.xml",
//                "META-INF/DEPENDENCIES",
//                "META-INF/LICENSE",
//                "META-INF/LICENSE.txt",
//                "META-INF/license.txt",
//                "META-INF/NOTICE",
//                "META-INF/NOTICE.txt",
//                "META-INF/notice.txt",
//                "META-INF/ASL2.0"
//            )
//        }
//        exclude("org/apache/commons/codec/language/*")
//        exclude("org/apache/commons/codec/language/bm/*")
//        exclude("android.content.res.XmlResourceParser")
//    }


    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    implementation(libs.xmlsec)
    implementation(libs.volley)
    implementation(libs.androidx.activity)
    // Otp Retriever Google Api
    implementation(libs.play.services.auth)
    implementation(libs.play.services.auth.api.phone)
    implementation(libs.play.services.location)
    implementation(libs.androidx.biometric)

//    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.zxing)
    implementation(libs.glide)
//    // Google Map
    implementation(libs.play.services.maps)

    implementation(libs.lottie)
//    // for OTP
     implementation(libs.otp.view)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.shimmer)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

//    // Swipe lib
    implementation(libs.slidetoact)

//    // In App Update
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)

    // Hilt
    implementation(libs.hilt.android)
//    implementation(libs.androidx.room.common)
    kapt(libs.hilt.compiler)

//    Qr code
    implementation (libs.core)
    implementation (libs.zxing.android.embedded)

    implementation("com.google.mlkit:barcode-scanning:17.0.2")
    implementation("androidx.camera:camera-camera2:1.0.0")
    implementation("androidx.camera:camera-lifecycle:1.0.0")
    implementation("androidx.camera:camera-view:1.0.0-alpha22")
}

