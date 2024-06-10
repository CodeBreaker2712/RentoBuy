package com.example.rentobuy.adapter

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
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.Cart
import com.example.rentobuy.model.WishListModel
import com.example.rentobuy.utils.Constants
import com.google.android.material.button.MaterialButton

/**Adapter for displaying expenses in RecyclerView **/
class WishListAdapter(

    //List for storing the details of the expenses of Expense Model class
    private val dataList: List<WishListModel>,

    //Listener for expenses
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<WishListAdapter.MyViewHolder>() {

    // ViewHolder class to hold the views for each expense in the RecyclerView
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Binding the textViews and editButton of layout file in the code using their respective ids
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val ll_move_cart: LinearLayout = itemView.findViewById(R.id.ll_move_cart)
        val ll_remove: LinearLayout = itemView.findViewById(R.id.ll_remove)
        val tvAmount: TextView = itemView.findViewById(R.id.tv_item_price)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        // Inflate the layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wish_item_row, parent, false)


        // Create and return a ViewHolder instance
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // Retrieve the expense data for the given position
        val expenseModel = dataList[position]

        // Bind the data to the ViewHolder
        holder.tvName.text = expenseModel.title
        holder.tvAmount.text = expenseModel.price

//        Bind the button with ViewHolder on click
        holder.ll_remove.setOnClickListener {
//            Item click is handled for the particular expense
            itemClickListener.onItemClick(expenseModel)
        }  //Bind the button with ViewHolder on click
        holder.ll_move_cart.setOnClickListener {
            val cartItem = Cart(
                Database().getUserID(),
                expenseModel.user_id,
                expenseModel.product_id,
                expenseModel.title,
                expenseModel.price,
                expenseModel.image,
                Constants.DEFAULT_CART_QUANTITY
            )
            Database().addItemToCart(cartItem, response = {
                if (it) {
                    Toast.makeText(holder.itemView.context, "Moved to cart", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(holder.itemView.context, "Error moved to cart", Toast.LENGTH_SHORT).show()
                }
            })
            itemClickListener.onItemClick(expenseModel)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    interface OnItemClickListener {

        //Callback method invoked when an item is clicked
        fun onItemClick(item: WishListModel)
    }
}