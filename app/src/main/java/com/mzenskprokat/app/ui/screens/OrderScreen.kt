package com.mzenskprokat.app.ui.screens

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.RemoveShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mzenskprokat.app.data.ProfileStorage
import com.mzenskprokat.app.models.OrderAttachment
import com.mzenskprokat.app.models.OrderRequest
import com.mzenskprokat.app.models.Result
import com.mzenskprokat.app.viewmodels.CartViewModel
import com.mzenskprokat.app.viewmodels.OrderViewModel
import com.mzenskprokat.app.data.OrderHistoryStorage
import com.mzenskprokat.app.models.OrderHistoryItem
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState

@Composable
fun OrderScreen(
    cartViewModel: CartViewModel,
    orderViewModel: OrderViewModel = viewModel()
) {
    var lastSubmittedItemsText by remember { mutableStateOf("") }
    var lastSubmittedComment by remember { mutableStateOf("") }
    var lastSubmittedAttachmentName by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val profileStorage = remember { ProfileStorage(context) }

    val orderHistoryStorage = remember { OrderHistoryStorage(context) }

    var comment by rememberSaveable { mutableStateOf("") }
    var selectedFileName by rememberSaveable { mutableStateOf("") }
    var selectedAttachment by remember { mutableStateOf<OrderAttachment?>(null) }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var localErrorMessage by remember { mutableStateOf<String?>(null) }

    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
    val orderState by orderViewModel.orderState.collectAsStateWithLifecycle()

    val profile = profileStorage.getProfile()
    val isProfileComplete = profile.name.isNotBlank() &&
            profile.phone.isNotBlank() &&
            profile.email.isNotBlank()

    val isLoading = orderState is Result.Loading
    val orderErrorMessage = (orderState as? Result.Error)?.message
    val errorMessage = localErrorMessage ?: orderErrorMessage
    val isSuccess = (orderState as? Result.Success)?.data == true

    val isFormValid by remember(profile.name, profile.phone, profile.email, cartItems) {
        derivedStateOf {
            isProfileComplete &&
                    cartItems.isNotEmpty() &&
                    cartItems.all { it.quantity.isNotBlank() }
        }
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult

        val fileName = queryFileName(context.contentResolver, uri) ?: "technical_specification"
        val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }

        if (bytes != null) {
            selectedAttachment = OrderAttachment(
                fileName = fileName,
                mimeType = mimeType,
                bytes = bytes
            )
            selectedFileName = fileName
        }
    }

    fun clearForm() {
        comment = ""
        selectedFileName = ""
        selectedAttachment = null
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            orderHistoryStorage.saveOrder(
                OrderHistoryItem(
                    id = System.currentTimeMillis(),
                    createdAt = System.currentTimeMillis(),
                    itemsText = lastSubmittedItemsText,
                    comment = lastSubmittedComment,
                    attachmentFileName = lastSubmittedAttachmentName,
                    status = "Отправлена"
                )
            )
            showSuccessDialog = true
        }
    }

    LaunchedEffect(orderErrorMessage) {
        if (!orderErrorMessage.isNullOrBlank()) {
            showErrorDialog = true
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (cartItems.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.RemoveShoppingCart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Корзина пуста",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        } else {
            items(
                items = cartItems,
                key = { "${it.productId}_${it.alloy}" }
            ) { item ->
                SwipeToDeleteCartItem(
                    onDelete = {
                        cartViewModel.removeFromCart(item.productId, item.alloy)
                    }
                ) {
                    CartItemCard(
                        productName = item.productName,
                        alloy = item.alloy,
                        quantity = item.quantity,
                        onQuantityChange = { newQuantity ->
                            cartViewModel.updateItemQuantity(item.productId, item.alloy, newQuantity)
                        }
                    )
                }
            }
        }

        item {
            Text(
                text = "Техническое задание",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (!isProfileComplete) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Профиль не заполнен",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Чтобы отправить заявку, заполните ФИО, телефон и email на странице «Профиль».",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Комментарий или описание ТЗ") },
                leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 6,
                enabled = !isLoading
            )
        }

        item {
            OutlinedButton(
                onClick = {
                    filePickerLauncher.launch(arrayOf("*/*"))
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Icon(Icons.Outlined.AttachFile, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (selectedFileName.isBlank()) {
                        "Загрузить файл ТЗ"
                    } else {
                        "Изменить файл ТЗ"
                    }
                )
            }
        }

        if (selectedFileName.isNotBlank()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedFileName,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        TextButton(
                            onClick = {
                                selectedAttachment = null
                                selectedFileName = ""
                            },
                            enabled = !isLoading
                        ) {
                            Text("Удалить")
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    if (!isProfileComplete) {
                        localErrorMessage = "Заполните профиль перед отправкой заявки"
                        showErrorDialog = true
                        return@Button
                    }

                    val cartText = cartItems.joinToString(separator = "\n") { item ->
                        "• ${item.productName}\n  Сплав: ${item.alloy}\n  Количество: ${item.quantity}"
                    }

                    val finalComment = buildString {
                        appendLine(cartText)

                        if (profile.isCompany) {
                            val hasAnyCompanyData =
                                profile.companyName.isNotBlank() ||
                                        profile.inn.isNotBlank() ||
                                        profile.kpp.isNotBlank() ||
                                        profile.city.isNotBlank() ||
                                        profile.position.isNotBlank()

                            if (hasAnyCompanyData) {
                                appendLine()
                                appendLine("Данные юридического лица:")

                                if (profile.companyName.isNotBlank()) {
                                    appendLine("Компания: ${profile.companyName}")
                                }
                                if (profile.inn.isNotBlank()) {
                                    appendLine("ИНН: ${profile.inn}")
                                }
                                if (profile.kpp.isNotBlank()) {
                                    appendLine("КПП: ${profile.kpp}")
                                }
                                if (profile.city.isNotBlank()) {
                                    appendLine("Город: ${profile.city}")
                                }
                                if (profile.position.isNotBlank()) {
                                    appendLine("Должность: ${profile.position}")
                                }
                            }
                        }

                        if (comment.isNotBlank()) {
                            append("\n\nКомментарий клиента:\n")
                            append(comment.trim())
                        }
                    }

                    val order = OrderRequest(
                        customerName = profile.name.trim(),
                        phone = profile.phone.trim(),
                        email = profile.email.trim(),
                        productName = "",
                        quantity = "",
                        comment = finalComment,
                        timestamp = System.currentTimeMillis(),
                        attachment = selectedAttachment
                    )

                    localErrorMessage = null
                    lastSubmittedItemsText = cartText
                    lastSubmittedComment = comment.trim()
                    lastSubmittedAttachmentName = selectedAttachment?.fileName
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

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                orderViewModel.resetOrderState()
                cartViewModel.clearCart()
                clearForm()
            },
            title = { Text("Заявка отправлена") },
            text = { Text("Менеджер получил вашу корзину и свяжется с вами.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        orderViewModel.resetOrderState()
                        cartViewModel.clearCart()
                        clearForm()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                localErrorMessage = null
                orderViewModel.resetOrderState()
            },
            title = { Text("Ошибка отправки") },
            text = {
                Text(errorMessage ?: "Попробуйте позже.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showErrorDialog = false
                        localErrorMessage = null
                        orderViewModel.resetOrderState()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun CartItemCard(
    productName: String,
    alloy: String,
    quantity: String,
    onQuantityChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = productName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Сплав: $alloy",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = quantity,
                onValueChange = onQuantityChange,
                label = { Text("Количество") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )
        }
    }
}

private fun queryFileName(contentResolver: ContentResolver, uri: Uri): String? {
    val cursor = contentResolver.query(uri, null, null, null, null) ?: return null
    cursor.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst() && nameIndex >= 0) {
            return it.getString(nameIndex)
        }
    }
    return null
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteCartItem(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 20.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        content = { content() }
    )
}