package com.mzenskprokat.app.viewmodels

import androidx.lifecycle.ViewModel
import com.mzenskprokat.app.models.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(item: CartItem) {
        _cartItems.update { current ->
            val exists = current.any {
                it.productId == item.productId && it.alloy == item.alloy
            }

            if (exists) {
                current
            } else {
                current + item
            }
        }
    }

    fun updateItemQuantity(productId: String, alloy: String, quantity: String) {
        _cartItems.update { current ->
            current.map { item ->
                if (item.productId == productId && item.alloy == alloy) {
                    item.copy(quantity = quantity)
                } else {
                    item
                }
            }
        }
    }

    fun removeFromCart(productId: String, alloy: String) {
        _cartItems.update { current ->
            current.filterNot {
                it.productId == productId && it.alloy == alloy
            }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun isInCart(productId: String, alloy: String?): Boolean {
        if (alloy.isNullOrBlank()) return false

        return _cartItems.value.any {
            it.productId == productId && it.alloy == alloy
        }
    }
}