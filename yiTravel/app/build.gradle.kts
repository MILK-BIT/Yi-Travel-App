import java.util.Properties
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "2.1.20-1.0.32" // KSP 插件
    id("org.jetbrains.kotlin.plugin.compose") // 添加这行
}
// 读取 local.properties 文件
val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.example.useai"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.useai"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val rawKey = localProperties.getProperty("DEEPSEEK_API_KEY", "").trim()
        // 验证密钥格式（示例：以 sk_ 开头）
        if (rawKey.isNotBlank() && !rawKey.startsWith("sk_")) {
            logger.warn("DEEPSEEK_API_KEY 格式可能不正确")
        }
        buildConfigField("String", "DEEPSEEK_API_KEY", "\"$rawKey\"")
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
    buildFeatures {
        compose = true
        buildConfig = true  // 确保这一行存在
    }

}

val roomVersion = "2.7.0"//在dependencies括号外定义

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
        implementation("com.squareup.okhttp3:okhttp:4.12.0")  // 使用最新稳定版
    implementation("androidx.navigation:navigation-compose:2.4.1")
    implementation ("androidx.compose.runtime:runtime-livedata:1.4.0")
    // Room 核心库
    implementation("androidx.room:room-runtime:$roomVersion")

    // 使用 KSP 替代 kapt 处理 Room 注解
    ksp("androidx.room:room-compiler:$roomVersion")

    // 可选 - Room 对 Kotlin 协程的支持
    implementation("androidx.room:room-ktx:$roomVersion")

    // 可选 - Room 测试支持
    testImplementation("androidx.room:room-testing:$roomVersion")

    implementation ("androidx.annotation:annotation:1.7.0")

    implementation("com.amap.api:3dmap:9.7.0")
    implementation("com.amap.api:search:9.7.0")

    implementation ("androidx.core:core-splashscreen:1.0.1")
}
