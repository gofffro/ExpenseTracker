package com.example.expensetracker.infrastructure.analytics

import com.example.expensetracker.core.analytics.AnalyticsService
import io.appmetrica.analytics.AppMetrica
import javax.inject.Inject

class AppMetricaAnalyticsService @Inject constructor() : AnalyticsService {
    override fun trackEvent(name: String, params: Map<String, Any>) {
        if (params.isEmpty()) {
            AppMetrica.reportEvent(name)
        } else {
            AppMetrica.reportEvent(name, params.mapValues { it.value.toString() })
        }
    }

    override fun trackError(message: String, error: Throwable?) {
        AppMetrica.reportError(message, error)
    }
}
