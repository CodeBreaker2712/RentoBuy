package com.example.rentobuy.modules.products


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.rentobuy.R
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.Product


class SellerProductDetailsActivity : AppCompatActivity() {
    private lateinit var productImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var stockQuantityTextView: TextView
    private lateinit var modifyButton: Button
    private lateinit var removeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_product_details)

        // Initialize views
        productImageView = findViewById(R.id.sellerProductImageView)
        titleTextView = findViewById(R.id.sellerTitleTextView)
        priceTextView = findViewById(R.id.sellerPriceTextView)
        descriptionTextView = findViewById(R.id.sellerDescriptionTextView)
        stockQuantityTextView = findViewById(R.id.sellerStockQuantityTextView)
        modifyButton = findViewById(R.id.sellerModifyButton)
        removeButton = findViewById(R.id.sellerRemoveButton)

        // Get product details from intent
        val product = intent.getParcelableExtra<Product>(EXTRA_PRODUCT)
        if (product != null) {
            // Set product details to views
            titleTextView.text = product.title
            priceTextView.text = "Price: $${product.price}"
            descriptionTextView.text = "Description: ${product.description}"
            stockQuantityTextView.text = "Stock Quantity: ${product.stock_quantity}"

            modifyButton.setOnClickListener {
                val modifyProductIntent = Intent(this, ModifySellerProductsActivity::class.java)
                modifyProductIntent.putExtra( EXTRA_PRODUCT, product)
                startActivity(modifyProductIntent)
                finish()
            }

            // Chat Button click Listener
            removeButton.setOnClickListener {
                Database().removeProductFromDB(product.product_id)
                Toast.makeText(this, "Product: ${product.title} successfully removed", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Failed to retrieve product details", Toast.LENGTH_SHORT).show()
            finish() // Finish activity if product details are not available
        }
    }

    companion object {
        const val EXTRA_PRODUCT = "extra_product"
        private const val TAG = "ProductDetailsActivity"

    }
}