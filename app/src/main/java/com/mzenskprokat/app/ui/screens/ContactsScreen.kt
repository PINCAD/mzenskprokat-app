package com.mzenskprokat.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.mzenskprokat.app.models.ContactInfo
import java.net.URLEncoder
import androidx.compose.foundation.layout.padding

@Composable
fun ContactsScreen() {
    val context = LocalContext.current
    val contactInfo = ContactInfo()

    val factoryMapUrl =
        "https://yandex.com/maps/10770/mtsensk/?ll=36.555907%2C53.272689&mode=poi&poi%5Bpoint%5D=36.550159%2C53.271509&poi%5Buri%5D=ymapsbm1%3A%2F%2Forg%3Foid%3D1007679740&z=16.19"

    val phoneDigits = contactInfo.phone.replace(Regex("[^0-9+]"), "")
    remember(contactInfo.address) {
        URLEncoder.encode(contactInfo.address, "UTF-8")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ContactsInfoBlock(
                phone = contactInfo.phone,
                email = contactInfo.email,
                website = contactInfo.website,
                onPhoneClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = "tel:$phoneDigits".toUri()
                    }
                    context.startActivity(intent)
                },
                onEmailClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:${contactInfo.email}".toUri()
                        putExtra(Intent.EXTRA_SUBJECT, "Запрос информации")
                    }
                    context.startActivity(intent)
                },
                onAddressClick = {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, factoryMapUrl.toUri())
                    )
                },
                onWebsiteClick = {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, contactInfo.website.toUri())
                    )
                }
            )
        }

        item {
            WorkingHoursCard()
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
@Composable
private fun ContactsInfoBlock(
    phone: String,
    email: String,
    website: String,
    onPhoneClick: () -> Unit,
    onEmailClick: () -> Unit,
    onAddressClick: () -> Unit,
    onWebsiteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Контактная информация",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            ContactInfoRow(
                icon = Icons.Outlined.Phone,
                title = "Телефон",
                value = phone,
                description = "Связаться с отделом продаж",
                onClick = onPhoneClick
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 14.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            ContactInfoRow(
                icon = Icons.Outlined.Email,
                title = "Электронная почта",
                value = email,
                description = "Отправить запрос или ТЗ",
                onClick = onEmailClick
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 14.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            ContactInfoRow(
                icon = Icons.Outlined.Place,
                title = "Адрес",
                value = "Металлургический завод Мценскпрокат",
                description = "Открыть в Яндекс Картах",
                onClick = onAddressClick
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 14.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            ContactInfoRow(
                icon = Icons.Filled.Language,
                title = "Веб-сайт",
                value = website,
                description = "Перейти на сайт компании",
                onClick = onWebsiteClick
            )
        }
    }
}
@Composable
private fun ContactInfoRow(
    icon: ImageVector,
    title: String,
    value: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(12.dp)
                    .size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ContactInfoCard(
    icon: ImageVector,
    title: String,
    value: String,
    description: String,
    onClick: (() -> Unit)?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .then(
                if (onClick != null) Modifier.clickable { onClick() }
                else Modifier
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(14.dp)
                        .size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (onClick != null) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun WorkingHoursCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(14.dp)
                            .size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Время работы",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "График работы отдела продаж",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    WorkingHoursLine(
                        day = "Понедельник — Пятница",
                        hours = "7:30 — 16:00"
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    WorkingHoursLine(
                        day = "Суббота — Воскресенье",
                        hours = "Выходной"
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkingHoursLine(
    day: String,
    hours: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = day,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = hours,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun openYandexMaps(context: android.content.Context, address: String) {
    val encoded = URLEncoder.encode(address, "UTF-8")
    val appUri = Uri.parse("yandexmaps://maps.yandex.ru/?text=$encoded")
    val webUri = Uri.parse("https://yandex.ru/maps/?text=$encoded")

    val intent = Intent(Intent.ACTION_VIEW, appUri)
    val fallbackIntent = Intent(Intent.ACTION_VIEW, webUri)

    runCatching {
        context.startActivity(intent)
    }.onFailure {
        context.startActivity(fallbackIntent)
    }
}