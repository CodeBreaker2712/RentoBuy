package com.example.rentobuy.modules.products

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.R
import com.example.rentobuy.adapter.ProductsListAdapter
import com.example.rentobuy.adapter.WishListAdapter
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.Cart
import com.example.rentobuy.model.Product
import com.example.rentobuy.modules.cart.CartActivity
import com.example.rentobuy.modules.chat.ChatActivity
import com.example.rentobuy.modules.wishlist.WishListActivity
import com.example.rentobuy.utils.Constants

class ProductsActivity : AppCompatActivity(),ProductsListAdapter.OnItemClickListener {

 val   productList: ArrayList<Product> = arrayListOf()
    lateinit var adapterProducts : ProductsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_products)


        val addProduct:Button = findViewById(R.id.addProduct)
        val goToCart:Button = findViewById(R.id.goToCart)
        val goToWish:Button = findViewById(R.id.goToWish)
        val goToChat:Button = findViewById(R.id.goToChat)

        goToCart.setOnClickListener {

            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)

        }
        goToWish.setOnClickListener {

            val intent = Intent(this, WishListActivity::class.java)
            startActivity(intent)

        }
        goToChat.setOnClickListener {

            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)

        }

        addProduct.setOnClickListener {

            val product = Product(
                Database().getUserID(),
                "unnati",
                "Product 2",
                "120",
                "Product details 2",
                "12",
                ""
            )

            Database().uploadProductDetails(product,response = {
                if (it) {
                    initView()
                    Toast.makeText(this, "Product Details updated.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Product Details error.", Toast.LENGTH_SHORT).show()
                }
            })

        }

        val rvProducts:RecyclerView = findViewById(R.id.rvProducts)

        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.setHasFixedSize(true)
        adapterProducts = ProductsListAdapter(productList,this)
        rvProducts.adapter = adapterProducts


        initView()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onItemClick(item: Product) {
       addToCart(item)
    }


    private fun addToCart(item: Product){
        val cartItem = Cart(
            Database().getUserID(),
            item.user_id,
            item.product_id,
            item.title,
            item.price,
            item.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        Database().addItemToCart(cartItem,response = {
            if (it) {
                initView()
                Toast.makeText(this, "Cart Item updated.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Cart Item error.", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun initView(){
        Database().getProductList(updateProductList =  {

            productList.clear()
            productList.addAll(it)
            adapterProducts?.notifyDataSetChanged()

        })

    }

}