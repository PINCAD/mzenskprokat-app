package com.mzenskprokat.app.repository

import android.util.Log
import com.mzenskprokat.app.BuildConfig
import com.mzenskprokat.app.api.TelegramApiService
import com.mzenskprokat.app.api.TelegramMessage
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TelegramNotificationService {

    companion object {
        private const val TAG = "TelegramService"
        private const val TELEGRAM_API_URL = "https://api.telegram.org/"
    }

    private val botToken: String = BuildConfig.TELEGRAM_BOT_TOKEN
    private val chatId: String = BuildConfig.TELEGRAM_CHAT_ID

    private val telegramApi: TelegramApiService by lazy {
        Retrofit.Builder()
            .baseUrl(TELEGRAM_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TelegramApiService::class.java)
    }

    suspend fun sendText(text: String): Result<Unit> {
        return try {
            if (botToken.isBlank() || botToken == "null" || chatId.isBlank() || chatId == "null") {
                return Result.failure(
                    IllegalStateException("Telegram config missing. Check local.properties / app build.gradle.kts")
                )
            }

            // ✅ Собираем безопасный URL: ":" в токене будет автоматически заэнкоден в %3A
            val fullUrl = TELEGRAM_API_URL
                .toHttpUrl()
                .newBuilder()
                .addPathSegment("bot$botToken")
                .addPathSegment("sendMessage")
                .build()
                .toString()

            val response = telegramApi.sendMessage(
                url = fullUrl,
                message = TelegramMessage(
                    chat_id = chatId,
                    text = text,
                    parse_mode = null
                )
            )

            Log.e(TAG, "Request URL: ${response.raw().request.url}")
            Log.e(TAG, "BOT_TOKEN length: ${botToken.length}, CHAT_ID=$chatId")

            if (!response.isSuccessful) {
                val err = response.errorBody()?.string()
                Log.e(TAG, "HTTP ${response.code()} errorBody=$err body=${response.body()}")
                Result.failure(RuntimeException("Telegram HTTP ${response.code()}"))
            } else {
                val body = response.body()
                if (body?.ok == true) {
                    Log.d(TAG, "Telegram: OK")
                    Result.success(Unit)
                } else {
                    Log.e(TAG, "Telegram ok=false description=${body?.description}")
                    Result.failure(RuntimeException("Telegram ok=false: ${body?.description}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception", e)
            Result.failure(e)
        }
    }
}
