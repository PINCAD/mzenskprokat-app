package com.mzenskprokat.app.models

data class OrderHistoryItem(
    val id: Long,
    val createdAt: Long,
    val itemsText: String,
    val comment: String,
    val attachmentFileName: String?,
    val status: String = "Отправлена"
)