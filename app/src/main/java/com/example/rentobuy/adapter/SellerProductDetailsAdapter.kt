package com.example.rentobuy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.R
import com.example.rentobuy.model.Product
import com.example.rentobuy.modules.products.ProductDetailsActivity
import com.example.rentobuy.modules.products.SellerProductDetailsActivity

class SellerProductDetailsAdapter(private val context: Context, private val productList: List<Product>) :
    RecyclerView.Adapter<SellerProductDetailsAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_recylerview, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        private val buttonFavorite: ImageButton = itemView.findViewById(R.id.buttonFavorite)

        fun bind(product: Product) {
            // Set product details to views
            textViewTitle.text = product.title
            textViewPrice.text = "Price: ${product.price}"
            // Set OnClickListener to navigate to SellerProductDetailsActivity
            itemView.setOnClickListener {
                val intent = Intent(context, SellerProductDetailsActivity::class.java)
                intent.putExtra(SellerProductDetailsActivity.EXTRA_PRODUCT, product)
                context.startActivity(intent)
            }
        }
    }
}