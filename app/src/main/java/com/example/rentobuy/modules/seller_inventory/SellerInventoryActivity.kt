package com.example.rentobuy.modules.seller_inventory

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.R
import com.example.rentobuy.adapter.RecentProductAdapter
import com.example.rentobuy.adapter.SellerProductDetailsAdapter
import com.example.rentobuy.database.Database
import com.example.rentobuy.modules.auth.ProfileActivity
import com.example.rentobuy.modules.chat.ChatListActivity
import com.example.rentobuy.modules.home.HomePageActivity
import com.example.rentobuy.modules.home.UserProfileActivity
import com.example.rentobuy.modules.notification.NotificationSettingsActivity
import com.example.rentobuy.modules.products.ProductUploadActivity
import com.google.android.material.navigation.NavigationView

class SellerInventoryActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var addProduct: ImageButton
    lateinit var chatFeature: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_inventory)

        addProduct = findViewById(R.id.btnAdd)
        chatFeature = findViewById(R.id.btnChat)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //enabling the drawer layout to be toggled
        drawerLayout = findViewById(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )

        //sync the state of toggling the drawer layout
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView : NavigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId){
                R.id.notificatonSettings -> {
                    val intentNotificationSettings = Intent(this, NotificationSettingsActivity::class.java)
                    startActivity(intentNotificationSettings)
                }

                R.id.switchToBuyer -> {
                    val switchBuyerIntent = Intent(this, HomePageActivity::class.java)
                    startActivity(switchBuyerIntent)
                }

                R.id.userProfile -> {
                    val notificationIntent = Intent(this, NotificationSettingsActivity::class.java)
                    startActivity(notificationIntent)
                }
            }
            true
        }

        //retrieve products from db
        retrieveProducts()

        addProduct.setOnClickListener(){
            //update to appropriate intents later
            val addProductIntent = Intent(this, ProductUploadActivity::class.java)
            startActivity(addProductIntent)
        }

        chatFeature.setOnClickListener(){
            //update to appropriate intents later
            val chatFeatureIntent = Intent(this, ChatListActivity::class.java)
            startActivity(chatFeatureIntent)
        }
    }
    private fun retrieveProducts() {
        val recyclerView = findViewById<RecyclerView>(R.id.seller_inventory_recycler_view)
        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager

        Database().getMyProducts(
            onSuccess = { productList ->
                val adapter = SellerProductDetailsAdapter(this, productList)
                recyclerView.adapter = adapter
            },
            onFailure = { exception ->
                Log.w(ContentValues.TAG, "Error Retrieving Products ", exception)
            }
        )
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        retrieveProducts()
    }
}