package com.example.rentobuy.modules.products

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rentobuy.R
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.Cart
import com.example.rentobuy.model.Product
import com.example.rentobuy.model.WishListModel
import com.example.rentobuy.modules.cart.CartActivity
import com.example.rentobuy.modules.chat.ChatActivity
import com.example.rentobuy.modules.home.HomePageActivity
import com.example.rentobuy.modules.home.UserProfileActivity
import com.example.rentobuy.modules.wishlist.WishListActivity
import com.example.rentobuy.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var productImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var stockQuantityTextView: TextView
    private lateinit var rentButton: Button
    private lateinit var chatButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        // Initialize views
        productImageView = findViewById(R.id.productImageView)
        titleTextView = findViewById(R.id.titleTextView)
        priceTextView = findViewById(R.id.priceTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        stockQuantityTextView = findViewById(R.id.stockQuantityTextView)
        rentButton = findViewById(R.id.rentButton)
        chatButton = findViewById(R.id.chatButton)

        bottomNavigationView = findViewById(R.id.bottomNavigationBar)

        val addToCartButton = findViewById<Button>(R.id.addToCartButton)
        val addToWishlistButton = findViewById<Button>(R.id.addToWishlistButton)

        println("onCreate: ProductDetailsActivity started")

        // Get product details from intent
        val product = intent.getParcelableExtra<Product>(EXTRA_PRODUCT)
        if (product != null) {
            // Set product details to views
            Glide.with(this)
                .load(product.image) // Assuming product.image is the URL stored in Firestore
                .placeholder(R.drawable.icon) // Placeholder image while loading
                .into(productImageView)

            titleTextView.text = product.title
            priceTextView.text = "Price: ${product.price}"
            descriptionTextView.text = "Description: ${product.description}"
            stockQuantityTextView.text = "Stock Quantity: ${product.stock_quantity}"
            // can load the image using Glide or Picasso library
            // Glide.with(this).load(product.image).into(productImageView)
            // Picasso.get().load(product.image).into(productImageView)

            // Rent button click listener
            rentButton.setOnClickListener {
                // Implement your rent functionality here
                Toast.makeText(this, "Rent button clicked", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Rent button clicked for product: ${product.title}")
            }

            addToCartButton.setOnClickListener {
                addToCart(product)
            }

            addToWishlistButton.setOnClickListener {
                addToWishList(product)
                // Implement add to wishlist functionality here
                Toast.makeText(this, "Add to Wishlist button clicked", Toast.LENGTH_SHORT).show()
            }

            // Chat Button click Listener
            chatButton.setOnClickListener {
                Toast.makeText(this, "Chat button clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
            }

        } else {
            Toast.makeText(this, "Failed to retrieve product details", Toast.LENGTH_SHORT).show()
            finish() // Finish activity if product details are not available
            Log.e(TAG, "Failed to retrieve product details")

        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> redirectToProfile()
                R.id.navigation_wishlist -> redirectToWishlist()
                R.id.navigation_cart -> redirectToCart()
                R.id.navigation_home -> redirectToHome()
            }
            true
        }
    }

    private fun addToWishList(item : Product){
        val wishListModel = WishListModel(
            Database().getUserID(),
            item.user_id,
            item.product_id,
            item.title,
            item.price,
            item.image
        )
        Database().addItemInWishList(wishListModel,response = {
            if (it) {
                Toast.makeText(this, "Wish List updated.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Wish List error.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addToCart(item: Product) {
        val userID = Database().getUserID() // Assuming this method retrieves the user ID
        val cartItem = Cart(
            userID,
            item.user_id,
            item.product_id,
            item.title,
            item.price,
            item.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        Database().addItemToCart(cartItem) { success ->
            if (success) {
                runOnUiThread {
                    Toast.makeText(this, "Cart Item updated.", Toast.LENGTH_SHORT).show()
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Cart Item error.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun redirectToProfile() {
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToWishlist() {
        val intent = Intent(this, WishListActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToCart() {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToHome(){
        val intent = Intent(this, HomePageActivity::class.java)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_PRODUCT = "extra_product"
        private const val TAG = "ProductDetailsActivity"

    }
}
