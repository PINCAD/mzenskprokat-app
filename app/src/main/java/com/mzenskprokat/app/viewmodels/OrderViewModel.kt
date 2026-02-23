package com.mzenskprokat.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mzenskprokat.app.models.OrderRequest
import com.mzenskprokat.app.models.Result
import com.mzenskprokat.app.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _orderState = MutableStateFlow<Result<Boolean>>(Result.Idle)
    val orderState: StateFlow<Result<Boolean>> = _orderState.asStateFlow()

    fun submitOrder(order: OrderRequest) {
        // Защита от повторных отправок
        if (_orderState.value is Result.Loading) return

        _orderState.value = Result.Loading
        viewModelScope.launch {
            repository.submitOrder(order).collect { result ->
                _orderState.value = result
            }
        }
    }

    fun resetOrderState() {
        _orderState.value = Result.Idle
    }
}