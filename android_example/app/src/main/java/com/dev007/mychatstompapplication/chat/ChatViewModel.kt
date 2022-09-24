package com.dev007.mychatstompapplication.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dev007.mychatstompapplication.dto.ChatMessageDto
import com.dev007.mychatstompapplication.utils.MessageAction
import com.dev007.mychatstompapplication.utils.TempVariables
import com.dev007.mychatstompapplication.utils.toJsonDto
import com.dev007.mychatstompapplication.utils.toJsonString
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompMessage
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    var mStompClient: StompClient = Stomp.over(
        Stomp.ConnectionProvider.JWS,
        "ws://192.168.43.226:8080/ws-chatapp" //JWS - javaWebSocket
    )
    val connect = mStompClient.connect()

    //main activity class purpose
    private var _userAdded: MutableLiveData<Boolean> = MutableLiveData()
    val userAdded: MutableLiveData<Boolean>
        get() = _userAdded

    //chat Fragment class purpose
    var listChat: MutableList<ChatMessageDto> = mutableListOf<ChatMessageDto>()
    //because of we using LiveData, instead of creating two list Variables, i created two boolean
    //variables, we can also use two list variables instead of Boolean in MutableLiveData.
    private var _onMessageReceived: MutableLiveData<Boolean> = MutableLiveData()
    val onMessageReceived: MutableLiveData<Boolean>
        get() = _onMessageReceived

    fun joinUser(userName: String) {
        TempVariables.currentUserName =
            userName //will use all application, in real case it will be unique and store in Prefs or datastore

        //receiving the message from topic
        mStompClient.topic("/topic/chat")
            .subscribe(::onReceiveMessagesFromSocket)

        //adding new user for login.
        val modelChatPayload = ChatMessageDto(
            chatUser = userName,
            messageAction = MessageAction.JOIN
        ).toJsonString()

        mStompClient.send("/app/addUser", modelChatPayload)
            .subscribeOn(Schedulers.io())
            .subscribe()

    }

    private fun onReceiveMessagesFromSocket(topicMessage: StompMessage) {
        Log.d("TAG", topicMessage.payload)


        runCatching {
            val chatModel = topicMessage.payload.toJsonDto() //if all good, it will do without crash

            if (chatModel.messageAction == MessageAction.JOIN &&
                (chatModel.chatUser == TempVariables.currentUserName)
            ) {
                _userAdded.postValue(true) //for navigating next screen
            }
            listChat.add(chatModel)
            _onMessageReceived.postValue(true)
            Log.d("TAG", "onReceiveMessagesFromSocket: chatModel $chatModel")
        }.onFailure {
            _onMessageReceived.postValue(false)
            Log.d("TAG", "onReceiveMessagesFromSocket: chatModel ERROR")
        }
    }

    fun sendMessage(s: String) {

        //sending new message
        val modelChatPayload = ChatMessageDto(
            chatUser = TempVariables.currentUserName,
            message = s,
            messageAction = MessageAction.MESSAGE
        ).toJsonString()

        mStompClient.send("/app/sendMessage", modelChatPayload)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun onCleared() {
        super.onCleared()
        mStompClient.disconnect()
    }


}
