package com.mzenskprokat.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mzenskprokat.app.models.OrderRequest
import com.mzenskprokat.app.models.Result
import com.mzenskprokat.app.viewmodels.OrderViewModel
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Send

@Composable
fun OrderScreen(
    orderViewModel: OrderViewModel = viewModel()
) {
    var customerName by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var productName by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }
    var comment by rememberSaveable { mutableStateOf("") }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    val orderState by orderViewModel.orderState.collectAsStateWithLifecycle()

    val isLoading = orderState is Result.Loading
    val errorMessage = (orderState as? Result.Error)?.message
    val isSuccess = (orderState as? Result.Success)?.data == true

    val isFormValid by remember(customerName, phone, email, productName, quantity) {
        derivedStateOf {
            customerName.isNotBlank() &&
                    phone.isNotBlank() &&
                    email.isNotBlank() &&
                    productName.isNotBlank() &&
                    quantity.isNotBlank()
        }
    }

    fun clearForm() {
        customerName = ""
        phone = ""
        email = ""
        productName = ""
        quantity = ""
        comment = ""
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) showSuccessDialog = true
    }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) showErrorDialog = true
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Оформление заказа",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Заполните форму, и мы свяжемся с вами",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Text("Контактные данные", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        item {
            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Ваше имя *") },
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )
        }

        item {
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Телефон *") },
                leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                placeholder = { Text("+7 (XXX) XXX-XX-XX") },
                enabled = !isLoading
            )
        }

        item {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email *") },
                leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                placeholder = { Text("example@mail.com") },
                enabled = !isLoading
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Информация о заказе", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        item {
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Наименование продукции *") },
                leadingIcon = { Icon(Icons.AutoMirrored.Outlined.List, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Например: Х20Н80") },
                enabled = !isLoading
            )
        }

        item {
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Количество *") },
                leadingIcon = { Icon(Icons.Outlined.Scale, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Например: 100 кг") },
                enabled = !isLoading
            )
        }

        item {
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Комментарий (необязательно)") },
                leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                placeholder = { Text("Дополнительная информация к заказу") },
                enabled = !isLoading
            )
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "После отправки заявки наш менеджер свяжется с вами для уточнения деталей и расчета стоимости заказа.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    val order = OrderRequest(
                        customerName = customerName.trim(),
                        phone = phone.trim(),
                        email = email.trim(),
                        productName = productName.trim(),
                        quantity = quantity.trim(),
                        comment = comment.trim(),
                        timestamp = System.currentTimeMillis()
                    )
                    orderViewModel.submitOrder(order)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Отправка...")
                } else {
                    Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Отправить заявку")
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                orderViewModel.resetOrderState()
                clearForm()
            },
            icon = {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("Заявка отправлена!") },
            text = { Text("Спасибо! Наш менеджер свяжется с вами в ближайшее время.") },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    orderViewModel.resetOrderState()
                    clearForm()
                }) { Text("Отлично") }
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                orderViewModel.resetOrderState()
            },
            icon = {
                Icon(
                    Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("Ошибка отправки") },
            text = { Text(errorMessage ?: "Произошла ошибка при отправке заказа. Попробуйте позже.") },
            confirmButton = {
                Button(onClick = {
                    showErrorDialog = false
                    orderViewModel.resetOrderState()
                }) { Text("OK") }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderScreenPreview() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("OrderScreen Preview")
        }
    }
}