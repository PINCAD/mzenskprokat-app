package com.mzenskprokat.app.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface TelegramApiService {

    @POST
    suspend fun sendMessage(
        @Url url: String,
        @Body message: TelegramMessage
    ): Response<TelegramResponse>
}

data class TelegramMessage(
    val chat_id: String,
    val text: String,
    val parse_mode: String? = null
)

data class TelegramResponse(
    val ok: Boolean,
    val description: String? = null
)
