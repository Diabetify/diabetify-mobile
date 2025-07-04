package com.itb.diabetify.util

object PredictionUpdateNotifier {
    private val listeners = mutableSetOf<() -> Unit>()

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    fun notifyPredictionUpdated() {
        listeners.forEach { it() }
    }

    fun clearAllListeners() {
        listeners.clear()
    }
}
