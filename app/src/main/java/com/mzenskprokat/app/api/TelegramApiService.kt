package com.mzenskprokat.app.api

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface TelegramApiService {

    @POST
    suspend fun sendMessage(
        @Url url: String,
        @Body message: TelegramMessage
    ): Response<TelegramResponse>

    @Multipart
    @POST
    suspend fun sendDocument(
        @Url url: String,
        @Part("chat_id") chatId: RequestBody,
        @Part document: MultipartBody.Part,
        @Part("caption") caption: RequestBody?
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