package com.mzenskprokat.app.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mzenskprokat.app.models.OrderHistoryItem

class OrderHistoryStorage(context: Context) {

    private val prefs = context.getSharedPreferences("order_history_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getOrders(): List<OrderHistoryItem> {
        val json = prefs.getString(KEY_ORDERS, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<OrderHistoryItem>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveOrder(order: OrderHistoryItem) {
        val current = getOrders().toMutableList()
        current.add(0, order)
        prefs.edit()
            .putString(KEY_ORDERS, gson.toJson(current))
            .apply()
    }

    fun clearOrders() {
        prefs.edit().remove(KEY_ORDERS).apply()
    }

    companion object {
        private const val KEY_ORDERS = "orders"
    }
}