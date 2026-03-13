package com.mzenskprokat.app.models

data class CartItem(
    val productId: String,
    val productName: String,
    val alloy: String,
    val quantity: String = ""
)