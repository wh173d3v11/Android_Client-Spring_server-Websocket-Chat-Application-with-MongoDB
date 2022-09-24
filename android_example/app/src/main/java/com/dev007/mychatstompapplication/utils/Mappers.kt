package com.dev007.mychatstompapplication.utils

import com.google.gson.Gson
import com.dev007.mychatstompapplication.dto.ChatMessageDto

fun ChatMessageDto.toJsonString(): String {
    return Gson().toJson(this)
}
fun String.toJsonDto(): ChatMessageDto {
    return Gson().fromJson(this,ChatMessageDto::class.java)
}