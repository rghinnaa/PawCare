package com.example.pawcare.utils.liveevent

open class Event<out T>(item: T) {
    private var hasHandle = false

    val data = if (hasHandle) { null } else { hasHandle = true; item }

    val peek = item
}