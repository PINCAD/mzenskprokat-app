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

// ViewModel для формы заказа
class OrderViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _orderState = MutableStateFlow<Result<Boolean>>(Result.Idle)
    val orderState: StateFlow<Result<Boolean>> = _orderState.asStateFlow()

    // Метод отправки заказа через репозиторий (как используется на экране OrderScreen)
    fun submitOrder(order: OrderRequest) {
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

// Вспомогательный класс состояния формы (оставляем здесь на случай использования)
data class OrderFormState(
    val customerName: String = "",
    val phone: String = "",
    val email: String = "",
    val productName: String = "",
    val quantity: String = "",
    val comment: String = "",
    val isSubmitting: Boolean = false,
    val error: String? = null
)