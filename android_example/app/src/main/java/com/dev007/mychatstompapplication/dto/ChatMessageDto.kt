package com.dev007.mychatstompapplication.dto

import com.dev007.mychatstompapplication.utils.TempVariables
import com.dev007.mychatstompapplication.utils.MessageAction

data class ChatMessageDto(
    val chatUser: String = TempVariables.currentUserName,
    val messageAction: MessageAction,
    val message: String? = null,
    val messageTime: String? = null,
)