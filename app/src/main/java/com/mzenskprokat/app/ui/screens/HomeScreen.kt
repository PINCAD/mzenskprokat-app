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

        // Герой-блок (оставил как у тебя)
        item {
            Card(
                shape = shape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp,
                        shadowElevation = 0.dp,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(id = R.drawable.mtsenk),
                                contentDescription = "Логотип",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Мценскпрокат",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "На главной — товары в наличии. В каталоге — подбор и заказ.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AssistChip(
                                onClick = onNavigateToCatalog,
                                label = { Text("Каталог") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.List,
                                        contentDescription = null
                                    )
                                }
                            )
                            AssistChip(
                                onClick = onNavigateToOrder,
                                label = { Text("Заказать") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.ShoppingCart,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
            }
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

        item { Spacer(modifier = Modifier.height(12.dp)) }

        item {
            Text(
                text = "Почему выбирают нас",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            FeatureCard(
                icon = Icons.Outlined.Star,
                title = "Качество продукции",
                description = "Соответствие ГОСТ, стабильные характеристики, контроль качества."
            )
        }

        item {
            FeatureCard(
                icon = Icons.Outlined.Info,
                title = "Консультации",
                description = "Подскажем по сплавам и применению, поможем подобрать аналог."
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
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