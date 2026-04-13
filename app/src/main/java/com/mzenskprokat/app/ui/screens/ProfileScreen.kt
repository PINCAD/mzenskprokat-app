package com.mzenskprokat.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mzenskprokat.app.data.OrderHistoryStorage
import com.mzenskprokat.app.data.ProfileStorage
import com.mzenskprokat.app.models.OrderHistoryItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.mzenskprokat.app.models.ProfileData

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val profileStorage = remember { ProfileStorage(context) }
    val savedProfile = remember { profileStorage.getProfile() }
    var lastSavedProfile by remember {
        mutableStateOf(savedProfile)
    }

    val orderHistoryStorage = remember { OrderHistoryStorage(context) }
    val orders = remember { orderHistoryStorage.getOrders() }

    var isCompany by rememberSaveable { mutableStateOf(lastSavedProfile.isCompany) }

    var name by rememberSaveable { mutableStateOf(lastSavedProfile.name) }
    var phone by rememberSaveable { mutableStateOf(lastSavedProfile.phone) }
    var email by rememberSaveable { mutableStateOf(lastSavedProfile.email) }

    var companyName by rememberSaveable { mutableStateOf(lastSavedProfile.companyName) }
    var inn by rememberSaveable { mutableStateOf(lastSavedProfile.inn) }
    var kpp by rememberSaveable { mutableStateOf(lastSavedProfile.kpp) }
    var city by rememberSaveable { mutableStateOf(lastSavedProfile.city) }
    var position by rememberSaveable { mutableStateOf(lastSavedProfile.position) }

    var savedMessage by rememberSaveable { mutableStateOf("") }

    val currentProfile = ProfileData(
        name = name.trim(),
        phone = phone.trim(),
        email = email.trim(),
        isCompany = isCompany,
        companyName = companyName.trim(),
        inn = inn.trim(),
        kpp = kpp.trim(),
        city = city.trim(),
        position = position.trim()
    )

    val hasChanges = currentProfile != lastSavedProfile

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Text(
                text = "Профиль",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Text(
                text = "Заполните данные один раз — они будут автоматически подставляться при оформлении заявки.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Тип клиента",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ProfileTypeCard(
                            modifier = Modifier.weight(1f),
                            title = "Физическое лицо",
                            selected = !isCompany,
                            onClick = {
                                isCompany = false
                                savedMessage = ""
                            }
                        )

                        ProfileTypeCard(
                            modifier = Modifier.weight(1f),
                            title = "Юридическое лицо",
                            selected = isCompany,
                            onClick = {
                                isCompany = true
                                savedMessage = ""
                            }
                        )
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            savedMessage = ""
                        },
                        label = { Text("ФИО") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            phone = it
                            savedMessage = ""
                        },
                        label = { Text("Телефон") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Phone,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            savedMessage = ""
                        },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    AnimatedVisibility(visible = isCompany) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = companyName,
                                onValueChange = {
                                    companyName = it
                                    savedMessage = ""
                                },
                                label = { Text("Название компании") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = inn,
                                onValueChange = {
                                    inn = it
                                    savedMessage = ""
                                },
                                label = { Text("ИНН") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = kpp,
                                onValueChange = {
                                    kpp = it
                                    savedMessage = ""
                                },
                                label = { Text("КПП") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = city,
                                onValueChange = {
                                    city = it
                                    savedMessage = ""
                                },
                                label = { Text("Город") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = position,
                                onValueChange = {
                                    position = it
                                    savedMessage = ""
                                },
                                label = { Text("Должность") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }
                    }

                    if (hasChanges) {
                        Button(
                            onClick = {
                                profileStorage.saveProfile(currentProfile)
                                lastSavedProfile = currentProfile
                                savedMessage = "Данные сохранены"
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Сохранить")
                        }
                    }

                    if (savedMessage.isNotBlank() && !hasChanges) {
                        Text(
                            text = savedMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "История заказов",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (orders.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = "У вас пока нет отправленных заявок.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(
                items = orders,
                key = { it.id }
            ) { order ->
                OrderHistoryCard(order = order)
            }
        }
    }
}

@Composable
private fun OrderHistoryCard(order: OrderHistoryItem) {
    val formatter = remember {
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru"))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Заказ от ${formatter.format(Date(order.createdAt))}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = order.status,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            HorizontalDivider()

            Text(
                text = order.itemsText,
                style = MaterialTheme.typography.bodyMedium
            )

            if (order.comment.isNotBlank()) {
                Text(
                    text = "Комментарий: ${order.comment}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!order.attachmentFileName.isNullOrBlank()) {
                Text(
                    text = "Файл ТЗ: ${order.attachmentFileName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
@Composable
private fun ProfileTypeCard(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = if (selected) "Выбрано" else " ",
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (selected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}