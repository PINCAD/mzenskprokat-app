@file:Suppress("unused")

package com.mzenskprokat.app.utils

import android.content.Context
import android.content.Intent
import android.util.Patterns
import androidx.core.net.toUri
import java.util.regex.Pattern

/**
 * Объект для валидации данных форм
 */
object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        val cleanPhone = phone.replace(Regex("[^0-9+]"), "")
        return cleanPhone.length >= 10
    }

    fun isValidName(name: String): Boolean {
        val namePattern = Pattern.compile("^[a-zA-Zа-яА-ЯёЁ\\s]+$")
        return name.isNotBlank() && namePattern.matcher(name).matches()
    }

    fun formatPhoneNumber(phone: String): String {
        val cleanPhone = phone.replace(Regex("[^0-9]"), "")
        return when {
            cleanPhone.length == 11 && cleanPhone.startsWith("7") -> {
                "+7 (${cleanPhone.substring(1, 4)}) ${cleanPhone.substring(4, 7)}-" +
                        "${cleanPhone.substring(7, 9)}-${cleanPhone.substring(9)}"
            }

            cleanPhone.length == 10 -> {
                "+7 (${cleanPhone.substring(0, 3)}) ${cleanPhone.substring(3, 6)}-" +
                        "${cleanPhone.substring(6, 8)}-${cleanPhone.substring(8)}"
            }

            else -> phone
        }
    }

    fun cleanPhoneNumber(phone: String): String {
        return phone.replace(Regex("[^0-9+]"), "")
    }
}

/**
 * Объект для работы с Intent'ами
 */
object IntentUtils {

    /**
     * Открыть приложение для звонка
     */
    fun makePhoneCall(context: Context, phoneNumber: String) {
        val cleanPhone = ValidationUtils.cleanPhoneNumber(phoneNumber)
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$cleanPhone".toUri()
        }
        context.safeStartActivity(intent)
    }

    /**
     * Открыть email клиент
     */
    fun sendEmail(
        context: Context,
        email: String,
        subject: String = "",
        body: String = ""
    ) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:$email".toUri()
            if (subject.isNotBlank()) putExtra(Intent.EXTRA_SUBJECT, subject)
            if (body.isNotBlank()) putExtra(Intent.EXTRA_TEXT, body)
        }
        // chooser иногда кидает исключения на некоторых устройствах без клиентов
        context.safeStartActivity(Intent.createChooser(intent, "Отправить email"))
    }

    /**
     * Открыть браузер
     */
    fun openWebsite(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = url.toUri()
        }
        context.safeStartActivity(intent)
    }

    /**
     * Поделиться текстом
     */
    fun shareText(context: Context, text: String, title: String = "Поделиться") {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.safeStartActivity(Intent.createChooser(intent, title))
    }
}

/**
 * Объект для форматирования данных
 */
object FormatUtils {

    fun formatAlloysList(alloys: List<String>, maxItems: Int = 5): String {
        return if (alloys.size <= maxItems) {
            alloys.joinToString(", ")
        } else {
            "${alloys.take(maxItems).joinToString(", ")} и еще ${alloys.size - maxItems}"
        }
    }

    fun truncateText(text: String, maxLength: Int): String {
        return if (text.length <= maxLength) text else "${text.substring(0, maxLength)}..."
    }
}

/**
 * Безопасный запуск Activity без resolveActivity().
 * Это убирает предупреждение про <queries> и надёжнее на разных устройствах.
 */
private fun Context.safeStartActivity(intent: Intent) {
    runCatching { startActivity(intent) }
}

/**
 * Extension функции для Context
 */
fun Context.makePhoneCall(phoneNumber: String) = IntentUtils.makePhoneCall(this, phoneNumber)
fun Context.sendEmail(email: String, subject: String = "", body: String = "") =
    IntentUtils.sendEmail(this, email, subject, body)

fun Context.openWebsite(url: String) = IntentUtils.openWebsite(this, url)
fun Context.shareText(text: String, title: String = "Поделиться") =
    IntentUtils.shareText(this, text, title)

/**
 * Extension функции для String
 */
fun String.isValidEmail(): Boolean = ValidationUtils.isValidEmail(this)
fun String.isValidPhone(): Boolean = ValidationUtils.isValidPhone(this)
fun String.isValidName(): Boolean = ValidationUtils.isValidName(this)
fun String.formatPhoneNumber(): String = ValidationUtils.formatPhoneNumber(this)
fun String.cleanPhoneNumber(): String = ValidationUtils.cleanPhoneNumber(this)