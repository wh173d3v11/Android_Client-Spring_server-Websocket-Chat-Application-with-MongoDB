package com.dev007.mychatstompapplication.chat

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dev007.mychatstompapplication.R
import com.dev007.mychatstompapplication.databinding.ItemChatMessageBinding
import com.dev007.mychatstompapplication.databinding.ItemUserJoinedBinding
import com.dev007.mychatstompapplication.dto.ChatMessageDto
import com.dev007.mychatstompapplication.utils.MessageAction
import com.dev007.mychatstompapplication.utils.TempVariables
import javax.inject.Inject

class ChatAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data: List<ChatMessageDto> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolderJoinLeft(var binding: ItemUserJoinedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(chatDO: ChatMessageDto) {
            binding.apply {
                if (chatDO.messageAction == MessageAction.LEAVE) {
                    tvUserJoined.text = chatDO.chatUser + " Left🙄😏😏..."
                } else {
                    val userName =
                        if (chatDO.chatUser == TempVariables.currentUserName) "You" else chatDO.chatUser
                    tvUserJoined.text = "$userName Joined😍🥰😉..."
                }
            }
        }
    }

    inner class ViewHolderChatMessage(var binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(chatDO: ChatMessageDto) {
            binding.apply {
                tvTime.text = chatDO.messageTime
                tvMessage.text = chatDO.message
                val isAnotherUserMessage =
                    (chatDO.chatUser.isNotEmpty()) && (chatDO.chatUser != TempVariables.currentUserName)
                llChat.gravity = if (isAnotherUserMessage) Gravity.START else Gravity.END
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_MESSAGE) {
            ViewHolderChatMessage(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_chat_message,
                    parent,
                    false
                )
            )
        } else {
            ViewHolderJoinLeft(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_user_joined,
                    parent,
                    false
                )
            )
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (data[position].messageAction == MessageAction.MESSAGE) VIEW_TYPE_MESSAGE
        else VIEW_TYPE_JOIN_LEFT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        if (holder is ViewHolderChatMessage) holder.onBind(data[position])
        else (holder as ViewHolderJoinLeft).onBind(data[position])

    override fun getItemCount() = data.size

    companion object {
        const val VIEW_TYPE_MESSAGE = 100
        const val VIEW_TYPE_JOIN_LEFT = 101
    }
}