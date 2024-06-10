package com.example.rentobuy.modules.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.R
import com.example.rentobuy.adapter.ChatAdapter
import com.example.rentobuy.services.AppMessagingService
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser

class ChatActivity : AppCompatActivity() {
    private lateinit var sellerId: String;
    private lateinit var senderId: String;
    private lateinit var userId: String;
    private lateinit var productId: String;
    private lateinit var productItem: String;
    private lateinit var productName: String;

    private lateinit var videoCallBtn: ZegoSendCallInvitationButton
    private lateinit var audioCallBtn: ZegoSendCallInvitationButton
    private lateinit var messageAdapter: ChatAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        productId = intent.getStringExtra("productId").toString()
        productItem = intent.getStringExtra("productItem").toString()
        productName = intent.getStringExtra("productName").toString()
        userId = intent.getStringExtra("userId").toString()
        sellerId = intent.getStringExtra("sellerId").toString()
        senderId = intent.getStringExtra("senderId").toString()

        val chatId = "${sellerId}_${userId}_${productId}"



        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val messageEditText: EditText = findViewById(R.id.messageEditText)
        // val messageEditText1: EditText = findViewById(R.id.messageEditText1)
        val usernameTextView: TextView = findViewById(R.id.usernameTextView)
        // Set the seller's ID as the text of the username TextView
        usernameTextView.text = sellerId



        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val messages = mutableListOf<ChatMessage>()
        messageAdapter = ChatAdapter(messages,senderId)
        recyclerView.adapter = messageAdapter


        val btnSend: Button = findViewById(R.id.btnSend)
        btnSend.setOnClickListener {
            val message = messageEditText.text.toString().trim()

            // val chatId = "3_1_1233210" // Replace with actual chat ID

            // val senderId = "1"

            // val userId = "1"

            // val sellerId = "3"

            // val productId = "1233210"

            val productName = "Nikul"

            val productImage = "https://example.com/product_image.jpg"

            if (message.isNotEmpty()) {
                sendMessage(
                    chatId,
                    senderId,
                    message,
                    userId,
                    sellerId,
                    productId,
                    productName,
                    productImage
                )
                messageEditText.text.clear()
            }
            /*
            val message = messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(chatId, senderId, message)
                messageEditText.text.clear()
            }*/
        }


        /*val btnSend1: Button = findViewById(R.id.btnSend1)
        btnSend1.setOnClickListener {
            val message = messageEditText1.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(chatId, userId, message)
                messageEditText1.text.clear()
            }
        }*/


        listenForMessages(chatId) { message ->
            // Handle incoming message
            Log.d("IncomingMessage", "Sender: ${message.senderId}, Message: ${message.message}")
            messages.add(message)
            messageAdapter.notifyItemInserted(messages.size - 1)
            recyclerView.scrollToPosition(messages.size - 1)

        }

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        videoCallServices(userId)


        videoCallBtn = ZegoSendCallInvitationButton(this)
        audioCallBtn = ZegoSendCallInvitationButton(this)
        videoCallBtn.setInvitees(listOf(ZegoUIKitUser(userId)))
        audioCallBtn.setInvitees(listOf(ZegoUIKitUser(userId)))

        videoCallBtn.setIsVideoCall(true)
        videoCallBtn.resourceID = "zego_uikit_call"

        audioCallBtn.setIsVideoCall(false)
        audioCallBtn.resourceID = "zego_uikit_call"

        findViewById<LinearLayout>(R.id.videoCallLayout).addView(videoCallBtn)
        findViewById<LinearLayout>(R.id.audioCallLayout).addView(audioCallBtn)

    }

    data class ChatMessage(
        val senderId: String = "",
        val message: String = "",
        val timestamp: Long = 0
    )

    data class Chat(
        var id: String = "",
        val sellerId: String = "",
        val userId: String = "",
        val productId: String = "",
        val productName: String = "",
        val productItem: String = ""
    )

    fun sendMessage(chatId: String, senderId: String, message: String, userId: String, sellerId: String, productId: String, productName: String, productImage: String) {
        val messageData = hashMapOf(
            "senderId" to senderId,
            "message" to message,
            "timestamp" to System.currentTimeMillis()
        )
        val db = FirebaseFirestore.getInstance()
        val notification = AppMessagingService()
        val chatData = hashMapOf(
            "userId" to userId,
            "sellerId" to sellerId,
            "productId" to productId,
            "productName" to productName,
            "productItem" to productImage
        )
        db.collection("chats").document(chatId)
            .set(chatData)
            .addOnSuccessListener {
                db.collection("chats").document(chatId)
                    .collection("messages").add(messageData)
                    .addOnSuccessListener { documentReference ->
                    }
                    .addOnFailureListener { e ->
                        // Handle error
                    }
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }




    fun sendMessage(chatId: String, senderId: String, message: String) {
        val messageData = hashMapOf(
            "senderId" to senderId,
            "message" to message,
            "timestamp" to System.currentTimeMillis()
        )

        val db = FirebaseFirestore.getInstance()
        val notification = AppMessagingService()
        db.collection("chats").document(chatId)
            .collection("messages").add(messageData)
            .addOnSuccessListener { documentReference ->
                notification.sendNotificationFromActivity(this,message, senderId)
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }

//    fun listenForMessages(chatId: String, messageListener: (ChatMessage) -> Unit) {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("chats").document(chatId)
//            .collection("messages")
//            .orderBy("timestamp")
//            .addSnapshotListener { querySnapshot, _ ->
//                querySnapshot?.documents?.forEach { document ->
//                    val chatMessage = document.toObject(ChatMessage::class.java)
//                    chatMessage?.let { messageListener(it) }
//                }
//            }
//    }

    fun listenForMessages(chatId: String, messageListener: (ChatMessage) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING) // Order by timestamp in descending order (latest first)
            //.limit(20) // Limit to 20 latest messages (you can adjust the limit as needed)
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.documentChanges?.forEach { change ->
                    if (change.type == DocumentChange.Type.ADDED) {
                        // New message added
                        val chatMessage = change.document.toObject(ChatMessage::class.java)
                        messageListener(chatMessage)
                        if (chatMessage.senderId !=  senderId) {
                            val notification = AppMessagingService()
                            notification.sendNotificationFromActivity(this, chatMessage.message, senderId)
                        }
                    }
                }
            }
    }
    private fun videoCallServices(userID: String) {
        val appID: Long = 1565205486
        val appSign = "71b8c2545063bcb837744e85ba5c8a8298c6dc471f495a0127584fc0c5b15889"
        val application = application
        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()
        callInvitationConfig.showDeclineButton = true
        val notificationConfig = ZegoNotificationConfig()
        notificationConfig.sound = "zego_uikit_sound_call"
        notificationConfig.channelID = "CallInvitation"
        notificationConfig.channelName = "CallInvitation"
        ZegoUIKitPrebuiltCallInvitationService.init(
            application,
            appID,
            appSign,
            userID,
            userID,
            callInvitationConfig
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        ZegoUIKitPrebuiltCallInvitationService.unInit()
    }

    private fun startVideoCall(receiverId: String) {
        videoCallBtn.setIsVideoCall(true)
        videoCallBtn.resourceID = "zego_uikit_call"
        videoCallBtn.setInvitees(listOf(ZegoUIKitUser(receiverId)))
    }

    private fun startAudioCall(receiverId: String) {
        audioCallBtn.setIsVideoCall(false)
        audioCallBtn.resourceID = "zego_uikit_call"
        audioCallBtn.setInvitees(listOf(ZegoUIKitUser(receiverId)))
    }


}