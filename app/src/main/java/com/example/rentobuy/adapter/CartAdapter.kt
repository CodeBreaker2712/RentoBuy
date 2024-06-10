package com.example.rentobuy.adapter

import android.annotation.SuppressLint
import android.content.Context
import com.example.rentobuy.R
import com.example.rentobuy.model.Product


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

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.model.Cart

/**Adapter for displaying expenses in RecyclerView **/
class CartAdapter(

    //List for storing the details of the expenses of Expense Model class
    private val dataList: List<Cart>,

    //Listener for expenses
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<CartAdapter.MyViewHolder>() {

    // ViewHolder class to hold the views for each expense in the RecyclerView
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Binding the textViews and editButton of layout file in the code using their respective ids
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvAmount: TextView = itemView.findViewById(R.id.tv_item_price)
        val tv_quantity: TextView = itemView.findViewById(R.id.tv_quantity)
        val ll_delete: LinearLayout = itemView.findViewById(R.id.ll_delete)
        val btnIncrease: ImageView = itemView.findViewById(R.id.btnIncrease)
        val btnDecrease: ImageView = itemView.findViewById(R.id.btnDecrease)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        // Inflate the layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item_row, parent, false)


        // Create and return a ViewHolder instance
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // Retrieve the expense data for the given position
        val expenseModel = dataList[position]

        // Bind the data to the ViewHolder
        holder.tvName.text = expenseModel.title
        holder.tvAmount.text ="$${expenseModel.price}"
        holder.tv_quantity.text = expenseModel.cart_quantity.toString()

        holder.ll_delete.setOnClickListener {
            itemClickListener.onItemClick(expenseModel,CartItemAction.removeFromCart)
        }


        holder.btnIncrease.setOnClickListener {
            itemClickListener.onItemClick(expenseModel,CartItemAction.increaseFromCart)
        }


        holder.btnDecrease.setOnClickListener {
            itemClickListener.onItemClick(expenseModel,CartItemAction.reduceFromCart)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    interface OnItemClickListener {

        //Callback method invoked when an item is clicked
        fun onItemClick(item: Cart, action: CartItemAction )
    }
}

enum class CartItemAction {
    removeFromCart,
    reduceFromCart,
    increaseFromCart
}