package com.mzenskprokat.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.CheckCircle
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

@Composable
fun HomeScreen(
    onNavigateToCatalog: () -> Unit,
    onNavigateToOrder: () -> Unit,
    onNavigateToProductDetail: (String) -> Unit
) {
    val shape = RoundedCornerShape(20.dp)

    // Продукты (для поиска + fallback поиска по названию)
    val repository = remember { ProductRepository() }
    val productsState by repository.getAllProducts()
        .collectAsStateWithLifecycle(initialValue = Result.Loading)

    val products = (productsState as? Result.Success<List<Product>>)?.data.orEmpty()

    // Поиск
    var query by rememberSaveable { mutableStateOf("") }
    val suggestions = remember(query, products) {
        if (query.isBlank()) emptyList()
        else products
            .asSequence()
            .filter { it.name.contains(query.trim(), ignoreCase = true) }
            .take(6)
            .toList()
    }

    fun openProductByName(productTitle: String) {
        val found = products.firstOrNull { p ->
            p.name.equals(productTitle, ignoreCase = true) ||
                    p.name.contains(productTitle, ignoreCase = true) ||
                    productTitle.contains(p.name, ignoreCase = true)
        }
        if (found != null) onNavigateToProductDetail(found.id) else onNavigateToCatalog()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Верхний блок: только Поиск
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
                        label = { Text("Поиск по товарам") },
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
                                Text("Открыть весь каталог")
                            }
                        }
                    }
                }
            }
        }

        // HERO
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
                            text = "Завод прецизионных сплавов • с 1964 года",
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

        // WHY US
        item { SectionTitle("Почему выбирают нас") }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    FeatureCard("60+ лет опыта", Icons.Outlined.Star, Modifier.weight(1f))
                    FeatureCard("ГОСТ качество", Icons.Outlined.CheckCircle, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    FeatureCard("150+ сплавов", Icons.AutoMirrored.Outlined.List, Modifier.weight(1f))
                    FeatureCard("Оптовые цены", Icons.Outlined.ShoppingCart, Modifier.weight(1f))
                }
            }
        }

        // PRODUCTS
        item { SectionTitle("Наша продукция") }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                CategoryRowNoLeftIcon(
                    title = "Прецизионные сплавы",
                    subtitle = "Высокое электрическое сопротивление",
                    onClick = { openProductByName("Прецизионные сплавы") }
                )
                CategoryRowNoLeftIcon(
                    title = "Магнитно-мягкие сплавы",
                    subtitle = "Высокая магнитная проницаемость",
                    onClick = { openProductByName("Магнитно-мягкие сплавы") }
                )
                CategoryRowNoLeftIcon(
                    title = "Проволока нихром",
                    subtitle = "Диаметр 0,1–10,0 мм",
                    onClick = { openProductByName("Проволока нихром") }
                )
                CategoryRowNoLeftIcon(
                    title = "Специальные стали",
                    subtitle = "Жаростойкие и коррозионностойкие",
                    onClick = { openProductByName("Специальные стали") }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun FeatureCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(92.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CategoryRowNoLeftIcon(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
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