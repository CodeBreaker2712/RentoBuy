package com.example.rentobuy.modules.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rentobuy.R

class PaymentActivity : AppCompatActivity() {
    lateinit var payNowBtn : Button
    lateinit var cardNum : EditText
    lateinit var expDate : EditText
    lateinit var cardHolderName : EditText
    lateinit var cvv : EditText
    lateinit var progressBar : ProgressBar
    lateinit var totalCost : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        payNowBtn = findViewById(R.id.pay_button)

        totalCost = findViewById(R.id.totalCost)
        val test = 23.123
        //Update this
        totalCost.text = "Total Cost: $${test}"


        payNowBtn.setOnClickListener(){

            cardNum = findViewById(R.id.editTextCardNumber)
            expDate = findViewById(R.id.editTextExpirationDate)
            cardHolderName = findViewById(R.id.editTextCardName)
            cvv = findViewById(R.id.editTextCVV)
            progressBar = findViewById(R.id.progressBar)


            //for demo purposes, only check if card holder name is equal to jane doe.
            //as this is a payment simulation, we cannot do proper verification checks with api's provided by google pay
            if(cardHolderName.text.toString() != "Jane Doe"){
                Toast.makeText(this, "Invalid Card Holder name", Toast.LENGTH_SHORT).show()
            }else{
                //simulate processing payment
                progressBar.visibility = View.VISIBLE

                //Logically, this is where you would call the backend API's to validate card details
                android.os.Handler().postDelayed({
                    Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()

                    //Redirect to product tracking activity (FAIZAN).
                    //Will probably need to implement helper function which
                    //generates a custom invoice based on the data
                    //change to correct screen later.
                    progressBar.visibility = View.GONE
                    val orderHistoryIntent = Intent(this, PaymentActivity::class.java)
                    startActivity(orderHistoryIntent)
                }, 5000)

            }
        }



    }
}