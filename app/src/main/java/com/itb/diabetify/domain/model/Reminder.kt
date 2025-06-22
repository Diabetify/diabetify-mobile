package com.itb.diabetify.domain.model

data class Reminder(
    val id: Int = 0,
    val title: String,
    val description: String,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) 