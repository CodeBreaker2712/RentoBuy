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
import com.example.rentobuy.model.Address
import com.example.rentobuy.model.Cart
import com.example.rentobuy.model.WishListModel
import com.example.rentobuy.utils.Constants
import com.google.android.material.button.MaterialButton

/**Adapter for displaying expenses in RecyclerView **/
class AddressAdapter(

    //List for storing the details of the expenses of Expense Model class
    private val dataList: List<Address>,

    //Listener for expenses
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<AddressAdapter.MyViewHolder>() {

    // ViewHolder class to hold the views for each expense in the RecyclerView
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Binding the textViews and editButton of layout file in the code using their respective ids
        val tv_label: TextView = itemView.findViewById(R.id.tv_label)
        val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        val tv_address: TextView = itemView.findViewById(R.id.tv_address)
        val ll_remove: LinearLayout = itemView.findViewById(R.id.ll_remove)
        val ll_select: LinearLayout = itemView.findViewById(R.id.ll_select)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        // Inflate the layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_row, parent, false)


        // Create and return a ViewHolder instance
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // Retrieve the expense data for the given position
        val expenseModel = dataList[position]

        // Bind the data to the ViewHolder
        holder.tv_label.text = expenseModel.addressLabel
        holder.tv_address.text = expenseModel.address
        holder.tv_name.text = expenseModel.name

        //Bind the button with ViewHolder on click
        holder.ll_remove.setOnClickListener {
            // Item click is handled for the particular expense
            itemClickListener.onDeleteAddress(expenseModel)
        }

        //Bind the button with ViewHolder on click
        holder.ll_select.setOnClickListener {
            itemClickListener.onSelectAddress(expenseModel)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    interface OnItemClickListener {

        //Callback method invoked when an item is clicked
        fun onDeleteAddress(item: Address)
        fun onSelectAddress(item: Address)
    }
}