package com.example.rentobuy.adapter


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.R
import com.example.rentobuy.model.Order
import com.example.rentobuy.modules.orders.OrdersDetailsActivity

class OrderAdapter(private val orders: List<Order>,val context:Activity) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)

        holder.itemView.setOnClickListener {
            val intent=Intent(context,OrdersDetailsActivity::class.java)
            intent.putExtra("order",order)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = orders.size

    class OrderViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(order: Order) {
           val titleView=view.findViewById<TextView>(R.id.tv_order_title)
            val priceView=view.findViewById<TextView>(R.id.tv_order_price)
            titleView.text=order.title
            priceView.text="Total: ${order.total_amount} INR"
        }
    }
}
