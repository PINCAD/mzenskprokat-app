package com.mzenskprokat.app.api

import com.google.gson.annotations.SerializedName
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
    @SerializedName("chat_id")
    val chatId: String,

    @SerializedName("text")
    val text: String,

    @SerializedName("parse_mode")
    val parseMode: String? = null
)

data class TelegramResponse(
    val ok: Boolean,
    val description: String? = null
)