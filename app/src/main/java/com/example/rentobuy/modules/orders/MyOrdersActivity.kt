package com.example.rentobuy.modules.orders

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.R
import com.example.rentobuy.adapter.OrderAdapter
import com.example.rentobuy.model.Address
import com.example.rentobuy.model.Cart
import com.example.rentobuy.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class MyOrdersActivity : ComponentActivity() {

    private lateinit var ordersRecyclerView: RecyclerView
    private val orders = mutableListOf<Order>()
    private var userId: String? = null
    private lateinit var backBtn: ImageButton
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)

        ordersRecyclerView = findViewById(R.id.rv_orders)
        backBtn = findViewById(R.id.btnBack)

        //close activity on back button click
        backBtn.setOnClickListener { finish() }

        ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        ordersRecyclerView.adapter = OrderAdapter(orders,this)


        userId = FirebaseAuth.getInstance().currentUser?.uid


        userId?.let {
            fetchOrders(it)
        } ?: run {
            Toast.makeText(this@MyOrdersActivity, "User not logged in.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun fetchOrders(userId: String) {

        db.collection("Orders")
            .whereEqualTo("user_id", userId)
            .get()
            .addOnSuccessListener { documents ->
                //hide progress bar
                findViewById<ProgressBar>(R.id.progressBar).visibility=View.GONE

                orders.clear() // Clear the orders before adding new ones
                for (document in documents) {
                    val order = document.toObject(Order::class.java).apply {
                        id = document.id // Set the document ID as the order ID
                    }
                    orders.add(order)
                }
                ordersRecyclerView.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting orders: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }




}
