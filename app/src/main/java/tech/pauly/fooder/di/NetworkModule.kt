package tech.pauly.fooder.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import tech.pauly.fooder.BuildConfig
import javax.inject.Singleton

private const val APP_ID = BuildConfig.EDAMAM_FOOD_APP_ID
private const val APP_KEY = BuildConfig.EDAMAM_FOOD_APP_KEY

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun providesOkHttpCallFactory(): Call.Factory =
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).addInterceptor { chain ->
                // Add Edamam App ID and App Key
                chain.request().let { request ->
                    val newUrl = request.url.newBuilder().apply {
                        addQueryParameter(name = "app_id", APP_ID)
                        addQueryParameter(name = "app_key", APP_KEY)
                    }.build()
                    chain.proceed(request.newBuilder().url(newUrl).build())
                }
            }.build()
}