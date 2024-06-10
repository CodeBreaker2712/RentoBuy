package com.example.rentobuy.modules.home;

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.rentobuy.R
import com.example.rentobuy.modules.auth.ProfileActivity
import com.example.rentobuy.modules.cart.CartActivity
import com.example.rentobuy.modules.notification.NotificationSettingsActivity
import com.example.rentobuy.modules.orders.MyOrdersActivity
import com.example.rentobuy.modules.seller_inventory.SellerInventoryActivity
import com.example.rentobuy.modules.wishlist.WishListActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserProfileActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var btnSwitchToSeller: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        bottomNavigationView = findViewById(R.id.bottomNavigationBar)
        val cardViewEditProfile: CardView = findViewById(R.id.cardEditProfile)
        val cardViewNotificationSettings: CardView = findViewById(R.id.cardNotification)
        val cardViewOrders: CardView = findViewById(R.id.cardOrders)
        val cardViewInviteFriends: CardView = findViewById(R.id.cardInviteFriends)
        btnSwitchToSeller = findViewById(R.id.btnSwitchToSeller)

        bottomNavigationView.selectedItemId = R.id.navigation_profile

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> redirectToProfile()
                R.id.navigation_wishlist -> redirectToWishlist()
                R.id.navigation_cart -> redirectToCart()
                R.id.navigation_home -> redirectToHome()
            }
            true
        }
        btnSwitchToSeller.setOnClickListener{
            val intent = Intent(this, SellerInventoryActivity::class.java)
            startActivity(intent)
        }

        cardViewEditProfile.setOnClickListener {
            val intentEditProfile = Intent(this, ProfileActivity::class.java)
            startActivity(intentEditProfile)
        }

        cardViewInviteFriends.setOnClickListener {
            inviteFriends()
        }

        cardViewNotificationSettings.setOnClickListener {
            // Intent to open the Notification Settings Activity
            val intentNotificationSettings = Intent(this, NotificationSettingsActivity::class.java)
            startActivity(intentNotificationSettings)
        }

        cardViewOrders.setOnClickListener {
            // Intent to open the Orders Activity
            val intentOrders = Intent(this, MyOrdersActivity::class.java)
            startActivity(intentOrders)
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
    fun inviteFriends() {
        val appLink = "https://play.google.com/store/apps/details?id=$packageName"
        val shareMessage = "${appLink}\nDownload this exciting RentoBuy mobile application to experience best products you can have."
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }

}

