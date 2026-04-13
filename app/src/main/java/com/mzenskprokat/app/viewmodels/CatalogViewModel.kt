package com.mzenskprokat.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mzenskprokat.app.models.CatalogData
import com.mzenskprokat.app.models.Product
import com.mzenskprokat.app.models.Result
import com.mzenskprokat.app.repository.ProductRepository
import com.mzenskprokat.app.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel для каталога продукции
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProductCatalogViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val refreshTrigger = MutableStateFlow(0)

    val searchQueryState: StateFlow<String> = searchQuery.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val products: StateFlow<Result<CatalogData>> =
        combine(
            searchQuery.map { it.trim() }.distinctUntilChanged(),
            refreshTrigger
        ) { query, _ -> query }
            .flatMapLatest { q -> repository.getCatalogData(q) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Result.Loading)

    init {
        refreshStock()
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun refreshStock() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                repository.refreshInStockProducts(Constants.STOCK_BASE_URL)
                refreshTrigger.value += 1
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}

/**
 * ViewModel для детального просмотра продукта
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModel(
    repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val productId = MutableStateFlow<String?>(null)

    val product: StateFlow<Result<Product>> =
        productId
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { id -> repository.getProductById(id) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Result.Loading)

    fun loadProduct(id: String) {
        productId.value = id
    }
}