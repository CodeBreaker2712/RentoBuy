package com.example.rentobuy.modules.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rentobuy.R
import com.example.rentobuy.model.User
import com.example.rentobuy.modules.home.HomePageActivity
import com.example.rentobuy.modules.home.UserProfileActivity
import com.example.rentobuy.modules.products.ProductsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextMobile: EditText
    private lateinit var editTextGender: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnLogout: Button
    private lateinit var btnBack: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize views
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextMobile = findViewById(R.id.editTextMobile)
        editTextGender = findViewById(R.id.editTextGender)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnLogout = findViewById(R.id.btnLogout)
        btnBack = findViewById(R.id.btnBack)

        // Set up user profile data
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val userEmail = user.email
            userEmail?.let { email ->
                firestore.collection("Users").document(email).get()
                    .addOnSuccessListener { documentSnapshot ->
                        val user = documentSnapshot.toObject(User::class.java)
                        user?.let { userData ->
                            editTextFirstName.setText(userData.firstName)
                            editTextLastName.setText(userData.lastName)
                            editTextEmail.setText(userData.email)
                            editTextMobile.setText(userData.mobile.toString())
                            editTextGender.setText(userData.gender)
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error retrieving user data: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Button click listeners
        btnUpdate.setOnClickListener {
            updateUserProfile()
        }

        btnLogout.setOnClickListener {
            logout()
        }

        btnBack.setOnClickListener{
            onBack()
        }
    }

    private fun updateUserProfile() {
        // Retrieve updated data from EditText fields
        val updatedFirstName = editTextFirstName.text.toString().trim()
        val updatedLastName = editTextLastName.text.toString().trim()
        val updatedMobile = editTextMobile.text.toString().trim().toLong()
        val updatedGender = editTextGender.text.toString().trim()

        // Update user data in Firestore
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val userEmail = user.email
            userEmail?.let { email ->
                val updatedUserData = hashMapOf(
                    "firstName" to updatedFirstName,
                    "lastName" to updatedLastName,
                    "mobile" to updatedMobile,
                    "gender" to updatedGender
                )

                firestore.collection("Users").document(email)
                    .update(updatedUserData as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(this, "User profile updated successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error updating user profile: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun logout() {
        firebaseAuth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    fun onBack() {
        onBackPressed()
    }
    override fun onBackPressed() {
        // Navigate to the previously opened activity
        super.onBackPressed()
    }

}
