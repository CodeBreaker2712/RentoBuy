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
import com.example.rentobuy.modules.chat.ChatActivity

/**Adapter for displaying expenses in RecyclerView **/
class ChatListAdapter(

    //List for storing the details of the expenses of Expense Model class
    private val dataList: List<ChatActivity.Chat>,

    //Listener for expenses
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ChatListAdapter.MyViewHolder>() {

    // ViewHolder class to hold the views for each expense in the RecyclerView
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Binding the textViews and editButton of layout file in the code using their respective ids
        val productName: TextView = itemView.findViewById(R.id.itemName)
        val sellerName: TextView = itemView.findViewById(R.id.sellerName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        // Inflate the layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item, parent, false)


        // Create and return a ViewHolder instance
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // Retrieve the expense data for the given position
        val expenseModel = dataList[position]

        // Bind the data to the ViewHolder
        holder.productName.text = expenseModel.productName
        holder.sellerName.text = expenseModel.sellerId

        //Bind the button with ViewHolder on click
        holder.itemView.setOnClickListener {

            //Item click is handled for the particular chat
            itemClickListener.onItemClick(expenseModel)
        }  //Bind the button with ViewHolder on click
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    interface OnItemClickListener {

        //Callback method invoked when an item is clicked
        fun onItemClick(item: ChatActivity.Chat)
    }
}