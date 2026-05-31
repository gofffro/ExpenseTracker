package com.example.expensetracker.infrastructure.error

import com.example.expensetracker.core.error.CrashReporter
import io.appmetrica.analytics.AppMetrica
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppMetricaCrashReporter @Inject constructor() : CrashReporter {
    override fun log(message: String) {
        AppMetrica.reportEvent(message)
    }

    override fun setKey(key: String, value: String) {
        // AppMetrica doesn't have a direct equivalent to custom keys in the same way Crashlytics does per-session,
        // but we can use reportEvent with parameters or environment variables.
        // For version 7.0.0+, putAppEnvironment might be located elsewhere or named differently if not in AppMetrica object
        // Let's try to report it as an event for now if putAppEnvironment is failing.
        AppMetrica.reportEvent("app_environment", mapOf(key to value))
    }

    override fun setUserId(userId: String?) {
        AppMetrica.setUserProfileID(userId)
    }

    override fun recordNonFatal(throwable: Throwable) {
        AppMetrica.reportError(throwable.message ?: "Non-fatal error", throwable)
    }
}
