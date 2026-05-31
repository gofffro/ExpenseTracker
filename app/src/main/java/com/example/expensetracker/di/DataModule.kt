package com.example.expensetracker.di

import android.content.Context
import com.example.expensetracker.data.ExpenseDao
import com.example.expensetracker.data.ExpenseDatabase
import com.example.expensetracker.data.remote.GigaChatApi
import com.example.expensetracker.data.repository.AiRepositoryImpl
import com.example.expensetracker.data.repository.ExpenseRepositoryImpl
import com.example.expensetracker.domain.repository.AiRepository
import com.example.expensetracker.domain.repository.ExpenseRepository
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
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return ExpenseDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao {
        return database.expenseDao()
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(dao: ExpenseDao): ExpenseRepository {
        return ExpenseRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideGigaChatApi(@ApplicationContext context: Context): GigaChatApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val cf = CertificateFactory.getInstance("X.509")
        
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
        }

        val certResources = listOf(
            com.example.expensetracker.data.R.raw.russian_trusted_root_ca,
            com.example.expensetracker.data.R.raw.russian_trusted_sub_ca
        )

        certResources.forEachIndexed { index, resId ->
            try {
                context.resources.openRawResource(resId).use { inputStream ->
                    val caCertificate = cf.generateCertificate(inputStream) as X509Certificate
                    keyStore.setCertificateEntry("ca_$index", caCertificate)
                }
            } catch (e: Exception) {
                android.util.Log.e("DataModule", "Failed to load cert $index", e)
            }
        }

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(keyStore)
        val trustManager = tmf.trustManagers.first() as X509TrustManager

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), null)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://gigachat.devices.sberbank.ru/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GigaChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAiRepository(api: GigaChatApi): AiRepository {
        return AiRepositoryImpl(api)
    }
}
