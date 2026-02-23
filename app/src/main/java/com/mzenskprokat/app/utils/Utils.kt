package com.mzenskprokat.app.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import java.util.regex.Pattern
/**
 * Объект для валидации данных форм
 */
object ValidationUtils {

    /**
     * Проверка email адреса
     */
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Проверка номера телефона
     */
    fun isValidPhone(phone: String): Boolean {
        val cleanPhone = phone.replace(Regex("[^0-9+]"), "")
        return cleanPhone.length >= 10
    }

    /**
     * Проверка имени (только буквы и пробелы)
     */
    fun isValidName(name: String): Boolean {
        val namePattern = Pattern.compile("^[a-zA-Zа-яА-ЯёЁ\\s]+$")
        return name.isNotBlank() && namePattern.matcher(name).matches()
    }

    /**
     * Форматирование номера телефона
     */
    fun formatPhoneNumber(phone: String): String {
        val cleanPhone = phone.replace(Regex("[^0-9]"), "")
        return when {
            cleanPhone.length == 11 && cleanPhone.startsWith("7") -> {
                "+7 (${cleanPhone.substring(1, 4)}) ${cleanPhone.substring(4, 7)}-${cleanPhone.substring(7, 9)}-${cleanPhone.substring(9)}"
            }
            cleanPhone.length == 10 -> {
                "+7 (${cleanPhone.substring(0, 3)}) ${cleanPhone.substring(3, 6)}-${cleanPhone.substring(6, 8)}-${cleanPhone.substring(8)}"
            }
            else -> phone
        }
    }

    /**
     * Очистка номера телефона для звонков
     */
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
            data = Uri.parse("tel:$cleanPhone")
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
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
            data = Uri.parse("mailto:$email")
            if (subject.isNotBlank()) {
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
            if (body.isNotBlank()) {
                putExtra(Intent.EXTRA_TEXT, body)
            }
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(Intent.createChooser(intent, "Отправить email"))
        }
    }

    /**
     * Открыть браузер
     */
    fun openWebsite(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    /**
     * Поделиться текстом
     */
    fun shareText(context: Context, text: String, title: String = "Поделиться") {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, title))
    }
}

/**
 * Объект для форматирования данных
 */
object FormatUtils {

    /**
     * Форматирование списка сплавов для отображения
     */
    fun formatAlloysList(alloys: List<String>, maxItems: Int = 5): String {
        return if (alloys.size <= maxItems) {
            alloys.joinToString(", ")
        } else {
            "${alloys.take(maxItems).joinToString(", ")} и еще ${alloys.size - maxItems}"
        }
    }

    /**
     * Обрезка текста с многоточием
     */
    fun truncateText(text: String, maxLength: Int): String {
        return if (text.length <= maxLength) {
            text
        } else {
            "${text.substring(0, maxLength)}..."
        }
    }
}

/**
 * Extension функции для Context
 */
fun Context.makePhoneCall(phoneNumber: String) {
    IntentUtils.makePhoneCall(this, phoneNumber)
}

fun Context.sendEmail(email: String, subject: String = "", body: String = "") {
    IntentUtils.sendEmail(this, email, subject, body)
}

fun Context.openWebsite(url: String) {
    IntentUtils.openWebsite(this, url)
}

fun Context.shareText(text: String, title: String = "Поделиться") {
    IntentUtils.shareText(this, text, title)
}

/**
 * Extension функции для String
 */
fun String.isValidEmail(): Boolean {
    return ValidationUtils.isValidEmail(this)
}

fun String.isValidPhone(): Boolean {
    return ValidationUtils.isValidPhone(this)
}

fun String.isValidName(): Boolean {
    return ValidationUtils.isValidName(this)
}

fun String.formatPhoneNumber(): String {
    return ValidationUtils.formatPhoneNumber(this)
}

fun String.cleanPhoneNumber(): String {
    return ValidationUtils.cleanPhoneNumber(this)
}