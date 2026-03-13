package com.mzenskprokat.app.models

data class OrderAttachment(
    val fileName: String,
    val mimeType: String,
    val bytes: ByteArray
)