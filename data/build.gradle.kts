plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.kostic_marko.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 27

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(project(":domain"))

    //Collections
    implementation(libs.immutable.collections)

    //coroutines
    implementation(libs.coroutines)

    //koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    //room
    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //ktor
    implementation(libs.ktor)
    implementation(libs.ktor.negotiation)
    implementation(libs.ktor.json.serialization)
    implementation(libs.ktor.logging)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}