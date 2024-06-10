package com.example.rentobuy.modules.home

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.rentobuy.R

class Appbar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.top_app_bar, this, true)

        val btnChat: ImageButton = findViewById(R.id.btnChat)
        val btnSwitchToSeller: Button = findViewById(R.id.btnSwitchToSeller)

        // Set onClick listeners
        btnChat.setOnClickListener {
            // Handle chat button click
            // Implement your logic here
        }

        btnSwitchToSeller.setOnClickListener {
            // Handle switch to seller button click
            // Implement your logic here
        }
    }
}
