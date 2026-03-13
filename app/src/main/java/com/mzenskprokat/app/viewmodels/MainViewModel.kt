package com.mzenskprokat.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mzenskprokat.app.models.*
import com.mzenskprokat.app.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

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
    repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val selectedCategory = MutableStateFlow<ProductCategory?>(null)
    private val searchQuery = MutableStateFlow("")

    val selectedCategoryState: StateFlow<ProductCategory?> = selectedCategory.asStateFlow()
    val searchQueryState: StateFlow<String> = searchQuery.asStateFlow()

    val products: StateFlow<Result<List<Product>>> =
        combine(selectedCategory, searchQuery) { cat, q -> cat to q.trim() }
            .distinctUntilChanged()
            .flatMapLatest { (cat, q) ->
                when {
                    q.isNotEmpty() -> repository.searchProducts(q)
                    cat != null -> repository.getProductsByCategory(cat)
                    else -> repository.getAllProducts()
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Result.Loading)

    fun loadAllProducts() {
        selectedCategory.value = null
        searchQuery.value = ""
    }

    fun filterByCategory(category: ProductCategory) {
        selectedCategory.value = category
        searchQuery.value = ""
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
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