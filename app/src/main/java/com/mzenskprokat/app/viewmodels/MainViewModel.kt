package com.mzenskprokat.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mzenskprokat.app.models.*
import com.mzenskprokat.app.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel для главного экрана
 */
class MainViewModel(
    repository: ProductRepository = ProductRepository()
) : ViewModel() {

    val homeData: StateFlow<Result<HomeData>> =
        repository.getHomeData()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Result.Loading)
}

/**
 * ViewModel для каталога продукции
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProductCatalogViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    companion object {
        private const val STOCK_BASE_URL =
            "https://script.google.com/macros/s/AKfycbw2REw35KBw_RSk9uxFYduMD9k4U75vUbAPoiZb4rhblXbhzUEVm58nhVGdEDx8lgLe/"
    }

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
                repository.refreshInStockProducts(STOCK_BASE_URL)
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