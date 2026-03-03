package com.mzenskprokat.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mzenskprokat.app.R
import com.mzenskprokat.app.models.Product
import com.mzenskprokat.app.models.Result
import com.mzenskprokat.app.repository.ProductRepository
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.delay
import androidx.compose.foundation.ExperimentalFoundationApi

private const val STOCK_BASE_URL =
    "https://script.google.com/macros/s/AKfycbw2REw35KBw_RSk9uxFYduMD9k4U75vUbAPoiZb4rhblXbhzUEVm58nhVGdEDx8lgLe/"

@Composable
fun HomeScreen(
    onNavigateToCatalog: () -> Unit,
    onNavigateToOrder: () -> Unit,
    onNavigateToProductDetail: (String) -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    val repository = remember { ProductRepository() }

    // ✅ Подтягиваем "в наличии" + (опционально) live-остатки
    val inStockState by repository
        .getInStockProductsWithLiveQty(STOCK_BASE_URL)
        .collectAsStateWithLifecycle(initialValue = Result.Loading)

    // ✅ Правильное извлечение списка из Result.Success<T>
    val inStockProducts: List<Product> =
        (inStockState as? Result.Success<List<Product>>)?.data ?: emptyList()

    var query by rememberSaveable { mutableStateOf("") }

    val suggestions = remember(query, inStockProducts) {
        val q = query.trim()
        if (q.isBlank()) emptyList()
        else inStockProducts
            .asSequence()
            .filter { p ->
                p.name.contains(q, ignoreCase = true) ||
                        p.alloys.any { it.contains(q, ignoreCase = true) }
            }
            .take(6)
            .toList()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Поиск (по товарам в наличии)
        item {
            Card(
                shape = shape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Поиск по товарам в наличии") },
                        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) }
                    )

                    if (suggestions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            suggestions.forEach { p ->
                                SuggestionRow(
                                    title = p.name,
                                    subtitle = p.description,
                                    onClick = {
                                        query = ""
                                        onNavigateToProductDetail(p.id)
                                    }
                                )
                            }

                            TextButton(
                                onClick = onNavigateToCatalog,
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Перейти в каталог (под заказ)")
                            }
                        }
                    }
                }
            }
        }

        // Рекламный блок (3 картинки, автоперелистывание 10 сек)
        item {
            AdBannerSlider(
                imageRes = listOf(
                    R.drawable.banner1,
                    R.drawable.banner2,
                    R.drawable.banner3
                ),
                intervalMs = 10_000L
            )
        }

        // Блок "В наличии"
        item {
            Text(
                text = "В наличии",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        when (val state = inStockState) {
            is Result.Loading, is Result.Idle -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is Result.Error -> {
                item {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            is Result.Success<*> -> {
                // ✅ Берём список уже из inStockProducts (он точно List<Product>)
                if (inStockProducts.isEmpty()) {
                    item {
                        Text(
                            "Сейчас нет товаров в наличии. Перейдите в каталог для заказа.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    items(
                        items = inStockProducts,
                        key = { it.id }
                    ) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onNavigateToProductDetail(product.id) }
                        )
                    }
                }
            }
        }
    }
}

// ниже твои FeatureCard/SuggestionRow без изменений
@Composable
private fun FeatureCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SuggestionRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdBannerSlider(
    imageRes: List<Int>,
    intervalMs: Long = 10_000L
) {
    val shape = RoundedCornerShape(20.dp)
    val pagerState = rememberPagerState(pageCount = { imageRes.size })

    LaunchedEffect(imageRes) {
        while (true) {
            delay(intervalMs)
            val next = (pagerState.currentPage + 1) % imageRes.size
            pagerState.animateScrollToPage(next)
        }
    }

    Card(
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) { page ->
            Image(
                painter = painterResource(id = imageRes[page]),
                contentDescription = "Реклама ${page + 1}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}