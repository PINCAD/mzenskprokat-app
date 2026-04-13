package com.mzenskprokat.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mzenskprokat.app.models.Product
import com.mzenskprokat.app.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.mzenskprokat.app.utils.Constants


class HomeViewModel : ViewModel() {

    private val repository = ProductRepository()

    val inStockProducts: StateFlow<List<Product>> =
        repository.observeInStockProducts()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = repository.observeInStockProducts().value
            )

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        refreshStock()
    }

    fun refreshStock() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                repository.refreshInStockProducts(Constants.STOCK_BASE_URL)
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}