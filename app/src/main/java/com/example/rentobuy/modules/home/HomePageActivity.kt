package com.example.rentobuy.modules.home

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.rentobuy.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.adapter.RecentProductAdapter
import com.example.rentobuy.database.Database
import com.example.rentobuy.modules.cart.CartActivity
import com.example.rentobuy.modules.chat.ChatListActivity
import com.example.rentobuy.modules.seller_inventory.SellerInventoryActivity
import com.example.rentobuy.modules.weather.WeatherActivity
import com.example.rentobuy.modules.wishlist.WishListActivity


class HomePageActivity : AppCompatActivity() {

    private lateinit var btnFilter: ImageButton
    private lateinit var btnWeather: ImageButton
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var btnSwitchToSeller: Button
    private lateinit var btnChat: ImageButton
    private lateinit var senderId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        senderId = intent.getStringExtra("senderId").toString()

        // Initialize components
        btnFilter = findViewById(R.id.btnFilter)
        btnWeather = findViewById(R.id.btnWeather)

        bottomNavigationView = findViewById(R.id.bottomNavigationBar)

        btnSwitchToSeller = findViewById(R.id.btnSwitchToSeller)

        btnChat = findViewById(R.id.btnChat)


        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnClickListener {
            // Intent to start the SearchActivity
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        btnFilter.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        btnWeather.setOnClickListener {
            val intent = Intent(this, WeatherActivity::class.java)
            startActivity(intent)
        }

        btnSwitchToSeller.setOnClickListener{
            val intent = Intent(this, SellerInventoryActivity::class.java)
            startActivity(intent)
        }

        btnChat.setOnClickListener{
            val intent = Intent(this, ChatListActivity::class.java)
            intent.putExtra("senderId", senderId)
            startActivity(intent)
        }

        // Setup slideshow for most recently added products
        setupSlideShow()

        // Setup for items you might like
        setupItemsUlike()

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

    private fun setupSlideShow() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewRecentlyAdded)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        Database().getRecentProducts(
            onSuccess = { productList ->
                val adapter = RecentProductAdapter(this, productList)
                recyclerView.adapter = adapter
            },
            onFailure = { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                // Handle failure case here if needed
            }
        )
    }

    private fun setupItemsUlike() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewYouMayLike)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        Database().getProductsInAscending(
            onSuccess = { productList ->
                val adapter = RecentProductAdapter(this, productList)
                recyclerView.adapter = adapter
            },
            onFailure = { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                // Handle failure case here if needed
            }
        )
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

}
