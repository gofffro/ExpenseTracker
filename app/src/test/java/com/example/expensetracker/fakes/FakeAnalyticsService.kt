package com.example.expensetracker.fakes

import com.example.expensetracker.core.analytics.AnalyticsService

class FakeAnalyticsService : AnalyticsService {
    val events = mutableListOf<Pair<String, Map<String, Any>>>()
    val errors = mutableListOf<Pair<String, Throwable?>>()

    override fun trackEvent(name: String, params: Map<String, Any>) {
        events.add(name to params)
        println("Tracked event: $name with params: $params")
    }

    override fun trackError(message: String, error: Throwable?) {
        errors.add(message to error)
        println("Tracked error: $message, error: $error")
    }
}
