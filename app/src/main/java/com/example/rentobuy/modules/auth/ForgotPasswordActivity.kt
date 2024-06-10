package com.example.rentobuy.modules.auth


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rentobuy.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var btnResetPassword: Button
    private lateinit var btnBackToLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail)
        btnResetPassword = findViewById(R.id.btnResetPassword)
        btnBackToLogin = findViewById(R.id.btnBackToLogin)

        btnResetPassword.setOnClickListener {
            val email = editTextEmail.text.toString().trim()

            resetPassword(email)
        }
        btnBackToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun resetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully
                    Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    // Error occurred, display a message to the user.
                    Toast.makeText(this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

