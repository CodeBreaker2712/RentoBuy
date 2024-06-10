package com.example.rentobuy.modules.orders

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.R
import com.example.rentobuy.adapter.ItemsAdapter
import com.example.rentobuy.adapter.OrderAdapter
import com.example.rentobuy.model.Cart
import com.example.rentobuy.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class OrdersDetailsActivity : ComponentActivity() {

    private lateinit var itemsRecyclerView: RecyclerView
    private val items = mutableListOf<Cart>()
    private var order: Order? = null
    private lateinit var backBtn: ImageButton
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order_details)

        itemsRecyclerView = findViewById(R.id.rv_items)
        backBtn = findViewById(R.id.btnBack)

        //close activity on back button click
        backBtn.setOnClickListener { finish() }


        order = intent.getParcelableExtra("order")

        itemsRecyclerView.layoutManager = LinearLayoutManager(this)
        itemsRecyclerView.adapter = order?.items?.let { ItemsAdapter(it) }


        val statusView=findViewById<TextView>(R.id.status)
        val subTotalView=findViewById<TextView>(R.id.subtotal)
        val totalView=findViewById<TextView>(R.id.total)
        val cancelBtn=findViewById<TextView>(R.id.cancelBtn)
        val trackBtn=findViewById<TextView>(R.id.trackBtn)

        statusView.text="Status: ${order?.status}"
        subTotalView.text="Subtotal: ${order?.sub_total_amount}"
        totalView.text="Total: ${order?.total_amount}"
        if(order?.status.equals("pending")){cancelBtn.visibility=View.VISIBLE}

        cancelBtn.setOnClickListener {
            order?.id?.let { it1 -> updateOrderStatus(it1,"canceled") }
            val intent = Intent(this, ReturnPageActivity::class.java)
            startActivity(intent)
        }

        trackBtn.setOnClickListener {
            startActivity(Intent(this,OrderTrackingActivity::class.java))
        }

    }


    fun updateOrderStatus(orderId: String, newStatus: String) {
        val db = FirebaseFirestore.getInstance()
        val orderRef = db.collection("Orders").document(orderId)
        orderRef.update("status", newStatus)
            .addOnSuccessListener {
                Toast.makeText(this@OrdersDetailsActivity,"Order Cancelled",Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener { e ->

            }
    }


}
