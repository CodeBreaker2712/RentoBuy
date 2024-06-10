package com.example.rentobuy.modules.wishlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.R
import com.example.rentobuy.adapter.CartAdapter
import com.example.rentobuy.adapter.WishListAdapter
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.Cart
import com.example.rentobuy.model.WishListModel
import com.example.rentobuy.modules.cart.CartActivity
import com.example.rentobuy.modules.home.HomePageActivity
import com.example.rentobuy.modules.home.UserProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class WishListActivity : AppCompatActivity() ,WishListAdapter.OnItemClickListener{

    var wishList: ArrayList<WishListModel> = arrayListOf()
    lateinit var rvProducts: RecyclerView

    lateinit var wishListAdapter: WishListAdapter
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wish_list)

        rvProducts = findViewById(R.id.rvWishlist)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        bottomNavigationView = findViewById(R.id.bottomNavigationBarWL)

        bottomNavigationView.selectedItemId = R.id.navigation_wishlist

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> redirectToProfile()
                R.id.navigation_wishlist -> redirectToWishlist()
                R.id.navigation_cart -> redirectToCart()
                R.id.navigation_home -> redirectToHome()
            }
            true
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.title = resources.getString(R.string.favourites)
        initView()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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

    fun initView(){
        Database().getWishList {
            wishList = it
            if (wishList.size > 0) {
                rvProducts.layoutManager = LinearLayoutManager(this)
                rvProducts.setHasFixedSize(true)
                wishListAdapter = WishListAdapter(wishList, this)
                rvProducts.adapter = wishListAdapter
            } else {
                rvProducts.visibility = View.GONE
            }

        }
    }
  
    override fun onItemClick(item: WishListModel) {
        Database().removeItemInWishList(this, item.id, response = {
            if (it) {
                initView()
                Toast.makeText(this, "WishListModel updated.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "WishListModel error.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}