package com.example.rentobuy.modules.products

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.rentobuy.R
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.Product
import org.w3c.dom.Text

class ModifySellerProductsActivity : AppCompatActivity() {
    lateinit var modifyBtn : Button
    lateinit var newPrice : String
    lateinit var newQuantity : String
    lateinit var newDescription : String
    lateinit var newTitle : String
    lateinit var titleTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_seller_products)


        val product = intent.getParcelableExtra<Product>(EXTRA_PRODUCT)
        titleTextView = findViewById(R.id.modifyProductTitleTextView)
        if (product != null) {
            newPrice = product.price
            newQuantity = product.stock_quantity
            newDescription = product.description
            newTitle = product.title

            titleTextView.text = product.title
        }
        modifyBtn = findViewById(R.id.sellerModifyButtonEdit)

        modifyBtn.setOnClickListener(){

            val modifyPrice = findViewById<EditText>(R.id.modifyPriceEditText).text.toString()
            val modifyDescription = findViewById<EditText>(R.id.modifyDescriptionEditText).text.toString()
            val modifyQuantity = findViewById<EditText>(R.id.modifyStockQuantityEditText).text.toString()
            val modifyTitle = findViewById<EditText>(R.id.modifyProductEditText).text.toString()

            if(modifyPrice.isNotEmpty()){
                newPrice = modifyPrice
            }
            if(modifyDescription.isNotEmpty()){
                newDescription = modifyDescription
            }
            if(modifyQuantity.isNotEmpty()){
                newQuantity = modifyQuantity
            }
            if(modifyTitle.isNotEmpty()){
                newTitle = modifyTitle
            }

            if (product != null) {
                Database().modifyProductInDB(product.product_id, newPrice, newTitle, newDescription, newQuantity)
                Toast.makeText(this, "Product: ${product.title} successfully modified", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Product modification failed", Toast.LENGTH_SHORT).show()
            }


        }


    }
    companion object {
        const val EXTRA_PRODUCT = "extra_product"

    }
}