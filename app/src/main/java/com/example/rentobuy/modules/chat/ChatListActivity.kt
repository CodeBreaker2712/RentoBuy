package com.example.rentobuy.modules.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.R
import com.example.rentobuy.adapter.ChatAdapter
import com.example.rentobuy.adapter.ChatListAdapter
import com.example.rentobuy.adapter.WishListAdapter
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.WishListModel

class ChatListActivity : AppCompatActivity() , ChatListAdapter.OnItemClickListener{


    var chatList: ArrayList<ChatActivity.Chat> = arrayListOf()
    lateinit var rvChatList: RecyclerView

    lateinit var chatListAdapter: ChatListAdapter
    private lateinit var btnBack: ImageButton
    private lateinit var senderId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_list)

        senderId = intent.getStringExtra("senderId").toString()

        rvChatList = findViewById(R.id.rvChatList)

        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener{
            super.onBackPressed()
        }

        initView()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatList)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun initView(){
        Database().getChatList(userId = "1") {
            chatList = it
            Log.e("TAgsa","This is size ${chatList.size}")
            if (chatList.size > 0) {
                rvChatList.layoutManager = LinearLayoutManager(this)
                rvChatList.setHasFixedSize(true)
                chatListAdapter = ChatListAdapter(chatList, this)
                rvChatList.adapter = chatListAdapter
            } else {
                rvChatList.visibility = View.GONE
            }

        }
    }

    override fun onItemClick(item: ChatActivity.Chat) {
        val intent = Intent(this,ChatActivity::class.java);
        intent.putExtra("productId", item.productId)
        intent.putExtra("productItem", item.productItem)
        intent.putExtra("productName", item.productName)
        intent.putExtra("userId", item.userId)
        intent.putExtra("sellerId", item.sellerId)
        intent.putExtra("senderId", senderId)
        startActivity(intent)
    }
}