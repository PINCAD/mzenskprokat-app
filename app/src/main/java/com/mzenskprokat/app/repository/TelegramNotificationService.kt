package com.mzenskprokat.app.repository

import android.util.Log
import com.mzenskprokat.app.BuildConfig
import com.mzenskprokat.app.api.TelegramApiService
import com.mzenskprokat.app.api.TelegramMessage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class TelegramNotificationService {

    companion object {
        private const val TAG = "TelegramService"
        private const val TELEGRAM_API_URL = "https://api.telegram.org/"
    }

    private val botToken: String = BuildConfig.TELEGRAM_BOT_TOKEN
    private val chatId: String = BuildConfig.TELEGRAM_CHAT_ID

    private val telegramApi: TelegramApiService by lazy {
        val okHttp = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                    addInterceptor(logging)
                }
            }
            .build()

        Retrofit.Builder()
            .baseUrl(TELEGRAM_API_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TelegramApiService::class.java)
    }

    suspend fun sendText(text: String): Result<Unit> {
        return try {
            validateConfig()

            val url = "${TELEGRAM_API_URL}bot$botToken/sendMessage"

            val response = telegramApi.sendMessage(
                url = url,
                message = TelegramMessage(
                    chatId = chatId,
                    text = text,
                    parseMode = null
                )
            )

            if (!response.isSuccessful) {
                val err = response.errorBody()?.string()
                Log.e(TAG, "Telegram HTTP ${response.code()} errorBody=$err")
                Result.failure(RuntimeException("Telegram HTTP ${response.code()}"))
            } else {
                val body = response.body()
                if (body?.ok == true) {
                    Result.success(Unit)
                } else {
                    Result.failure(RuntimeException("Telegram ok=false: ${body?.description}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Telegram exception", e)
            Result.failure(e)
        }
    }

    suspend fun sendDocument(
        fileName: String,
        mimeType: String,
        bytes: ByteArray,
        caption: String? = null
    ): Result<Unit> {
        return try {
            validateConfig()

            val url = "${TELEGRAM_API_URL}bot$botToken/sendDocument"

            val chatIdPart = chatId.toRequestBody("text/plain".toMediaTypeOrNull())
            val captionPart = caption?.toRequestBody("text/plain".toMediaTypeOrNull())
            val mediaType = mimeType.toMediaTypeOrNull() ?: "application/octet-stream".toMediaTypeOrNull()

            val documentPart = MultipartBody.Part.createFormData(
                name = "document",
                filename = fileName,
                body = bytes.toRequestBody(mediaType)
            )

            val response = telegramApi.sendDocument(
                url = url,
                chatId = chatIdPart,
                caption = captionPart,
                document = documentPart
            )

            if (!response.isSuccessful) {
                val err = response.errorBody()?.string()
                Log.e(TAG, "Telegram document HTTP ${response.code()} errorBody=$err")
                Result.failure(RuntimeException("Telegram document HTTP ${response.code()}"))
            } else {
                val body = response.body()
                if (body?.ok == true) {
                    Result.success(Unit)
                } else {
                    Result.failure(RuntimeException("Telegram document ok=false: ${body?.description}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Telegram document exception", e)
            Result.failure(e)
        }
    }

    private fun validateConfig() {
        if (botToken.isBlank() || botToken == "null" || chatId.isBlank() || chatId == "null") {
            throw IllegalStateException(
                "Telegram config missing. Check local.properties / app build.gradle.kts"
            )
        }
    }
}