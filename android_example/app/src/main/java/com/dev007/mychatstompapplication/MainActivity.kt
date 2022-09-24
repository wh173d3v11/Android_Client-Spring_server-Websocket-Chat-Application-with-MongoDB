package com.dev007.mychatstompapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.transition.Slide
import com.dev007.mychatstompapplication.chat.ChatFragment
import com.dev007.mychatstompapplication.chat.ChatViewModel
import com.dev007.mychatstompapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import ua.naiksoftware.stomp.dto.LifecycleEvent

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val vm: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUi()
        setObservers()
        vm.connect
    }

    private fun setObservers() {
        vm.mStompClient.lifecycle()
            .subscribe { lifecycleEvent: LifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> {
                        runOnUiThread {
                            Toast.makeText(this, "Stomp connection opened", Toast.LENGTH_SHORT)
                                .show()
                        }
                        Log.d("TAG", "Stomp connection opened")
                    }
                    LifecycleEvent.Type.ERROR -> {
                        runOnUiThread {
                            Toast.makeText(
                                this,
                                "Error ${lifecycleEvent.exception}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        Log.e("TAG", "Error", lifecycleEvent.exception)
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        runOnUiThread {
                            Toast.makeText(this, "Stomp connection closed", Toast.LENGTH_SHORT)
                                .show()
                        }
                        Log.d("TAG", "Stomp connection closed")
                    }
                    else -> {
                        Log.d("TAG", "Stomp connection - EVENT NOT FOUND ")
                    }
                }
            }

        vm.userAdded.observe(this) {
            if (it == true) {
                openChatFragment()
            } else {
                Toast.makeText(this, "User Not Added, please check server.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun openChatFragment() {
        //opening Chat Fragment.
        val frag = ChatFragment().apply {
            enterTransition = Slide()
            exitTransition = Slide()
        }
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, frag)
            .disallowAddToBackStack()
            .commit()
        binding.apply {
            root.postDelayed({ isFragmentShowing = true }, 200)
        }
    }

    private fun setUi() {
        binding.isFragmentShowing = false
        binding.btnConnect.setOnClickListener {
            val username = (binding.etUserName.text?.toString()) ?: ""
            if (username.isEmpty()) {
                Toast.makeText(this, "Username shouldn't EMPTY", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            vm.joinUser(username)
        }
    }
}