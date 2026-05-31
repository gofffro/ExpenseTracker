package com.example.expensetracker.di

import com.example.expensetracker.core.analytics.AnalyticsService
import com.example.expensetracker.core.auth.AuthService
import com.example.expensetracker.core.error.CrashReporter
import com.example.expensetracker.core.profile.ProfileService
import com.example.expensetracker.core.remoteconfig.RemoteConfigService
import com.example.expensetracker.infrastructure.analytics.AppMetricaAnalyticsService
import com.example.expensetracker.infrastructure.auth.AuthServiceImpl
import com.example.expensetracker.infrastructure.error.CompositeCrashReporter
import com.example.expensetracker.infrastructure.error.FirebaseCrashReporter
import com.example.expensetracker.infrastructure.profile.FirebaseProfileService
import com.example.expensetracker.infrastructure.remoteconfig.FirebaseRemoteConfigService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun bindCrashReporter(
        crashReporter: CompositeCrashReporter
    ): CrashReporter

    @Binds
    @Singleton
    abstract fun bindAnalyticsService(
        analyticsService: AppMetricaAnalyticsService
    ): AnalyticsService

    @Binds
    @Singleton
    abstract fun bindAuthService(
        authService: AuthServiceImpl
    ): AuthService

    @Binds
    @Singleton
    abstract fun bindRemoteConfigService(
        remoteConfigService: FirebaseRemoteConfigService
    ): RemoteConfigService

    @Binds
    @Singleton
    abstract fun bindProfileService(
        profileService: FirebaseProfileService
    ): ProfileService
}
