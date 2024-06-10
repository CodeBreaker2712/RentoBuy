package com.example.rentobuy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.R
import com.example.rentobuy.modules.chat.ChatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter(
    private val messages: List<ChatActivity.ChatMessage>,
    private val senderId: String
) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val chatDate: TextView = itemView.findViewById(R.id.chatDate)
        val chatTime: TextView = itemView.findViewById(R.id.chatTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutRes = if (viewType == TYPE_SENDER) R.layout.item_message_sender else R.layout.item_message_receiver
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageTextView.text = message.message
        // Convert timestamp to Date object
        val date = Date(message.timestamp)

        // Format date in "10 June 2023" format
        val dateFormatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        holder.chatDate.text = dateFormatter.format(date)

        // Format time in "8:22pm" format
        val timeFormatter = SimpleDateFormat("h:mma", Locale.getDefault())
        holder.chatTime.text = timeFormatter.format(date)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == senderId) TYPE_SENDER else TYPE_RECEIVER
    }

    companion object {
        private const val TYPE_SENDER = 1
        private const val TYPE_RECEIVER = 2
    }
}
