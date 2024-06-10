package com.example.rentobuy.modules.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rentobuy.R
import com.example.rentobuy.model.User
import com.example.rentobuy.modules.home.HomePageActivity
import com.example.rentobuy.modules.products.ProductsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextMobile: EditText
    private lateinit var editTextGender: EditText
    private lateinit var btnSignUp: Button
    private lateinit var btnBackToLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize views
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextMobile = findViewById(R.id.editTextMobile)
        editTextGender = findViewById(R.id.editTextGender)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnBackToLogin = findViewById(R.id.btnBackToLogin)

        btnSignUp.setOnClickListener {
            val firstName = editTextFirstName.text.toString().trim()
            val lastName = editTextLastName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val mobile = editTextMobile.text.toString().trim().toLong()
            val gender = editTextGender.text.toString().trim()

            signUp(firstName, lastName, email, password, mobile, gender)
        }

        btnBackToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun signUp(firstName: String, lastName: String, email: String, password: String, mobile: Long, gender: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    val user = User(
                        id = firebaseUser!!.uid,
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        mobile = mobile,
                        gender = gender,
                        profileCompleted = 1
                    )

                    // Save user data to Firestore
                    firestore.collection("Users")
                        .document(email)
                        .set(user)
                        .addOnSuccessListener {
                            // Send user details back to MainActivity
                            val intent = Intent(this, HomePageActivity::class.java)
                            intent.putExtra("user", user)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(baseContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
