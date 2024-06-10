package com.example.rentobuy.modules.orders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.rentobuy.R
import com.example.rentobuy.modules.chat.ChatListActivity
import com.example.rentobuy.modules.seller_inventory.SellerInventoryActivity

class ReturnPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_page)

        val reasonInput : EditText = findViewById(R.id.reasonInput)
        val returnButton : Button = findViewById(R.id.returnButton)
        val btnSwitchToSeller : Button = findViewById(R.id.btnSwitchToSeller)
        val btnChat : ImageButton = findViewById(R.id.btnChat)

        returnButton.setOnClickListener {
            Toast.makeText(
                this,
                "Refund has been requested",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, ReturnPageActivity::class.java)
            startActivity(intent)
        }

        btnSwitchToSeller.setOnClickListener {
            val intent = Intent(this, SellerInventoryActivity::class.java)
            startActivity(intent)
        }

        btnChat.setOnClickListener {
            val intent = Intent(this, ChatListActivity::class.java)
            startActivity(intent)
        }
    }
}