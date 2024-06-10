package com.example.rentobuy

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.example.rentobuy.modules.auth.LoginActivity
import com.example.rentobuy.modules.auth.ProfileActivity
import com.example.rentobuy.modules.home.HomePageActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()


        if (firebaseAuth.currentUser != null) {
            // User is already logged in, navigate to ProfileActivity
            startActivity(Intent(this, HomePageActivity::class.java))

            finish() // Finish MainActivity after navigating to ProfileActivity
        } else {
            // User is not logged in, navigate to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Finish MainActivity after navigating to LoginActivity
        }
    }

}
