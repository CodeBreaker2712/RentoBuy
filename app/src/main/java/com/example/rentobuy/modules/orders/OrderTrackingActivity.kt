package com.example.rentobuy.modules.orders

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.rentobuy.R
import com.example.rentobuy.model.Order


class OrderTrackingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        val backBtn = findViewById<ImageView>(R.id.btnBack)
        //close activity on back button click
        backBtn.setOnClickListener { finish() }

        // Display order tracking information
        findViewById<TextView>(R.id.txtOrderId).text = "Order ID: #0989809"
        findViewById<TextView>(R.id.txtTrackingId).text = "Tracking ID: #Fed98765"
        findViewById<TextView>(R.id.txtOrderStatus).text = "Status: Shiped"
        findViewById<TextView>(R.id.txtOrderDate).text = "Order Date: 24 March 2024, 02:00 PM"
        findViewById<TextView>(R.id.txtDispatched).text = "Dispatched at: 26 March 2024, 10:00 AM"
        findViewById<TextView>(R.id.txtArrived).text = "Arrived at: On the Way"
        findViewById<TextView>(R.id.txtDelivery).text = "Expected Delivery: 31 March 2024"
    }


}
