package com.example.rentobuy.adapter

import android.content.Context
import com.example.rentobuy.R
import com.example.rentobuy.model.Product
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.Cart
import com.example.rentobuy.model.WishListModel
import com.example.rentobuy.utils.Constants


/**Adapter for displaying expenses in RecyclerView **/
class ProductsListAdapter(

    //List for storing the details of the expenses of Expense Model class
    private val dataList: List<Product>,

    //Listener for expenses
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ProductsListAdapter.MyViewHolder>() {

    // ViewHolder class to hold the views for each expense in the RecyclerView
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Binding the textViews and editButton of layout file in the code using their respective ids
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvAmount: TextView = itemView.findViewById(R.id.tv_item_price)
        val ibAddItem: ImageButton = itemView.findViewById(R.id.ibAddItem)
        val ibWishList: ImageButton = itemView.findViewById(R.id.ibWishList)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        // Inflate the layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item_row, parent, false)


        // Create and return a ViewHolder instance
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // Retrieve the expense data for the given position
        val expenseModel = dataList[position]

        // Bind the data to the ViewHolder
        holder.tvName.text = expenseModel.title
        holder.tvAmount.text = expenseModel.price

        //Bind the button with ViewHolder on click
        holder.ibAddItem.setOnClickListener {

            //Item click is handled for the particular expense
            itemClickListener.onItemClick(expenseModel)
        }  //Bind the button with ViewHolder on click
        holder.ibWishList.setOnClickListener {
            val wishListModel = WishListModel(
                Database().getUserID(),
                expenseModel.user_id,
                expenseModel.product_id,
                expenseModel.title,
                expenseModel.price,
                expenseModel.image
            )
            Database().addItemInWishList(wishListModel,response = {
                if (it) {
                    Toast.makeText(holder.itemView.context, "Wish List updated.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(holder.itemView.context, "Wish List error.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    interface OnItemClickListener {

        //Callback method invoked when an item is clicked
        fun onItemClick(item: Product)
    }
}