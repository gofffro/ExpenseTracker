package com.example.gigaitest.di

import android.content.Context
import com.example.gigaitest.R
import com.example.gigaitest.data.api.AuthInterceptor
import com.example.gigaitest.data.api.GigaChatApi
import com.example.gigaitest.data.api.GigaOAuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object GigaChatNetworkModule {

    private const val OAUTH_BASE_URL = "https://ngw.devices.sberbank.ru:9443/"
    private const val API_BASE_URL = "https://gigachat.devices.sberbank.ru/"

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @Named("baseOkHttpClient")
    fun provideBaseOkHttpClient(
        @ApplicationContext context: Context,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
        }

        val certResources = listOf(
            R.raw.russian_trusted_root_ca,
            R.raw.russian_trusted_sub_ca
        )

        certResources.forEachIndexed { index, resId ->
            try {
                context.resources.openRawResource(resId).use { inputStream ->
                    if (inputStream.available() > 0) {
                        val caCertificate = certificateFactory.generateCertificate(inputStream) as X509Certificate
                        keyStore.setCertificateEntry("ca_$index", caCertificate)
                    }
                }
            } catch (e: Exception) {
            }
        }

        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)
        val trustManager = trustManagerFactory.trustManagers.first() as X509TrustManager

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), null)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .build()
    }

    @Provides
    @Singleton
    @Named("apiOkHttpClient")
    fun provideApiOkHttpClient(
        @Named("baseOkHttpClient") baseOkHttpClient: OkHttpClient,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return baseOkHttpClient.newBuilder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("oauthRetrofit")
    fun provideOauthRetrofit(@Named("baseOkHttpClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(OAUTH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("apiRetrofit")
    fun provideApiRetrofit(@Named("apiOkHttpClient") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGigaOAuthApi(@Named("oauthRetrofit") retrofit: Retrofit): GigaOAuthApi {
        return retrofit.create(GigaOAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGigaChatApi(@Named("apiRetrofit") retrofit: Retrofit): GigaChatApi {
        return retrofit.create(GigaChatApi::class.java)
    }
}
