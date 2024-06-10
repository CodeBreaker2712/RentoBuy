package com.example.rentobuy.adapter

import com.example.rentobuy.R


//open class ProductsListAdapter(private val context: Context, private var list: ArrayList<Product>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.product_item_row, parent, false))
//    }
//
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val model = list[position]
//
//        if (holder is ProductViewHolder) {
//
//            holder.itemView.tv_item_name.text = model.title
//            holder.itemView.tv_item_price.text = "$${model.price}"
//
//
////            holder.itemView.ib_delete_product.setOnClickListener {
////                fragment.deleteProduct(model.product_id)
////            }
//
//            holder.itemView.setOnClickListener {
//
//            }
//
//        }
//    }
//
//
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//}

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.model.Cart


class ItemsAdapter(
    private val dataList: List<Cart>,
) :
    RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName: TextView = itemView.findViewById(R.id.tv_item_title)
        val tvAmount: TextView = itemView.findViewById(R.id.tv_item_price)
        val tv_quantity: TextView = itemView.findViewById(R.id.tv_item_quantity)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_item, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = dataList[position]

        holder.tvName.text = item.title
        holder.tvAmount.text = item.price
        holder.tv_quantity.text = "Quantity: ${item.cart_quantity.toString()}"

    }

    override fun getItemCount(): Int {
        return dataList.size
    }



}