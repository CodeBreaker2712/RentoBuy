package com.example.rentobuy.modules.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
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
import com.example.rentobuy.adapter.CartItemAction
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.Cart
import com.example.rentobuy.model.Product
import com.example.rentobuy.modules.delivery.AddressSelectionActivity
import com.example.rentobuy.modules.home.HomePageActivity
import com.example.rentobuy.modules.home.UserProfileActivity
import com.example.rentobuy.modules.payment.PaymentActivity
import com.example.rentobuy.modules.wishlist.WishListActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.play.integrity.internal.al
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/***
 *
 * todo //Add product should be update product
 *
 * Done
 */
class CartActivity : AppCompatActivity(), CartAdapter.OnItemClickListener {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    var cartList: ArrayList<Cart> = arrayListOf()
    lateinit var cartAdapter: CartAdapter
    lateinit var rvProducts: RecyclerView
    lateinit var tv_sub_total: TextView
    lateinit var tv_shipping_charge: TextView
    lateinit var tv_tax: TextView
    lateinit var btn_checkout: Button
    lateinit var ll_checkout: LinearLayout
    lateinit var tv_total_amount: TextView
    lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var lastProductsList: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

        rvProducts = findViewById(R.id.rvCarts)
        tv_sub_total = findViewById(R.id.subtotal_value)
        tv_shipping_charge = findViewById(R.id.delivery_value)
        tv_tax = findViewById(R.id.tax_value)
        tv_total_amount = findViewById(R.id.tv_total_amount)
        btn_checkout = findViewById(R.id.btn_checkout)

        ll_checkout = findViewById(R.id.ll_checkout)

        bottomNavigationView = findViewById(R.id.bottomNavigationBar)

        btn_checkout.setOnClickListener {
            val intent = Intent(this, AddressSelectionActivity::class.java)
            startActivity(intent)
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.title = "Cart"

        initView()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bottomNavigationView.selectedItemId = R.id.navigation_cart

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> redirectToProfile()
                R.id.navigation_wishlist -> redirectToWishlist()
                R.id.navigation_cart -> redirectToCart()
                R.id.navigation_home -> redirectToHome()
            }
            true
        }
    }

    override fun onItemClick(item: Cart, action: CartItemAction) {
        when (action) {
            CartItemAction.removeFromCart -> {
                Database().removeItemInCart( item.id, response = {
                    if (it) {
                        initView()
                        Toast.makeText(this, "CartItem updated.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "CartItem error.", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            CartItemAction.reduceFromCart -> {
                deleteCart(item)
            }

            CartItemAction.increaseFromCart -> {
                addCart(item)
            }
        }
    }

    private fun addCart(item: Cart) {
        val cartQuantity: Int = item.cart_quantity.toInt()
        val itemHashMap = HashMap<String, Any>()
        itemHashMap["cart_quantity"] = (cartQuantity + 1).toString()
        Database().updateCartList(this, item.id, itemHashMap, response = {
            if (it) {
                initView()
                Toast.makeText(this, "CartItem updated.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "CartItem error.", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun deleteCart(item: Cart) {
        if (item.cart_quantity == "1") {
            Database().removeItemInCart( item.id, response = {
                if (it) {
                    initView()
                    Toast.makeText(this, "CartItem updated.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "CartItem error.", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            val cartQuantity: Int = item.cart_quantity.toInt()
            val itemHashMap = HashMap<String, Any>()
            itemHashMap["cart_quantity"] = (cartQuantity - 1).toString()
            Database().updateCartList(this, item.id, itemHashMap, response = {
                if (it) {
                    initView()
                    Toast.makeText(this, "CartItem updated.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "CartItem error.", Toast.LENGTH_SHORT).show()

                }
            })
        }
    }

    private fun initView() {
        Database().getCartList(updateProductList = {
            cartList = it
            if (cartList.size > 0) {
                rvProducts.layoutManager = LinearLayoutManager(this)
                rvProducts.setHasFixedSize(true)
                cartAdapter = CartAdapter(cartList, this)
                rvProducts.adapter = cartAdapter
                var subTotal: Double = 0.0
                for (item in cartList) {
                        val price = item.price.toDouble()
                        val quantity = item.cart_quantity.toInt()
                        subTotal += (price * quantity)
                }

                tv_sub_total.text = "$$subTotal"

                var deliveryCharge = 0.08 * subTotal // 8% delivery charge
                val tax = 0.05 * subTotal // 5% tax

                deliveryCharge = if (deliveryCharge > 20) 20.0 else deliveryCharge
                tv_shipping_charge.text = "$$deliveryCharge" //change the cargo fee logic here.
                tv_tax.text = "$$tax" //change the cargo fee logic here.

                if (subTotal > 0) {
                    btn_checkout.visibility = View.VISIBLE
                    ll_checkout.visibility = View.VISIBLE
                    val total = subTotal + 10
                    tv_total_amount.text = "$$total"
                } else {
                    btn_checkout.visibility = View.GONE
                    ll_checkout.visibility = View.GONE
                }
            } else {
                rvProducts.visibility = View.GONE
                btn_checkout.visibility = View.GONE
                ll_checkout.visibility = View.GONE
            }

        })
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
}