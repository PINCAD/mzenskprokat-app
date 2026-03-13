package com.mzenskprokat.app.utils

fun pluralAlloys(count: Int): String {
    val mod10 = count % 10
    val mod100 = count % 100

    return when {
        mod10 == 1 && mod100 != 11 -> "$count сплав"
        mod10 in 2..4 && mod100 !in 12..14 -> "$count сплава"
        else -> "$count сплавов"
    }
}