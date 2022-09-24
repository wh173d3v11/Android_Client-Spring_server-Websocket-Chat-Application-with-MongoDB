package com.dev007.mychatstompapplication.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev007.mychatstompapplication.databinding.FragChatBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {

    var _binding: FragChatBinding? = null
    val binding: FragChatBinding
        get() = _binding!!

    private val vm: ChatViewModel by activityViewModels()

    @Inject
    lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragChatBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        setUi()
    }

    private fun setObserver() {
        vm.onMessageReceived.observe(viewLifecycleOwner) {
            if (it == true) {
                refreshRvAdapter()
            }
        }
    }

    private fun refreshRvAdapter() {
        chatAdapter.data = vm.listChat
        binding.isChatEmpty = vm.listChat.isEmpty()
        if (vm.listChat.isNotEmpty()) binding.rvChat.scrollToPosition(vm.listChat.size - 1)
    }

    private fun setUi() {
        binding.apply {
            isChatEmpty = vm.listChat.isEmpty()
            rvChat.adapter = chatAdapter
            (rvChat.layoutManager as LinearLayoutManager).stackFromEnd = true
            ivSend.setOnClickListener {
                vm.sendMessage(etChat.text?.toString() ?: "")
                etChat.setText("")
                etChat.clearFocus()
            }
            refreshRvAdapter()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}