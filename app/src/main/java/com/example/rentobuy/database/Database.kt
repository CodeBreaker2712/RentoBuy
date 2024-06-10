package com.example.rentobuy.database

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.rentobuy.model.Address
import com.example.rentobuy.model.Cart
import com.example.rentobuy.model.Order
import com.example.rentobuy.model.Product
import com.example.rentobuy.model.WishListModel
import com.example.rentobuy.modules.cart.CartActivity
import com.example.rentobuy.modules.chat.ChatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject

class Database {

    private val db = FirebaseFirestore.getInstance()

    fun getRecentProducts(onSuccess: (List<Product>) -> Unit, onFailure: (Exception) -> Unit) {
        val query = db.collection("products")
            .orderBy("product_id", Query.Direction.DESCENDING)
            .limit(20)

        query.get()
            .addOnSuccessListener { documents ->
                val productList = mutableListOf<Product>()
                for (document in documents) {
                    val product = document.toObject(Product::class.java)
                    productList.add(product)
                }
                onSuccess(productList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getMyProducts(onSuccess: (List<Product>) -> Unit, onFailure: (Exception) -> Unit) {
        val userId = getUserID()
        val query = db.collection("products").whereEqualTo("user_id", userId)

        query.get()
            .addOnSuccessListener { documents ->
                val productList = mutableListOf<Product>()
                for (document in documents) {
                    val product = document.toObject(Product::class.java)
                    productList.add(product)
                }
                onSuccess(productList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun removeProductFromDB(productId: String){
        db.collection("products").document(productId)
            .delete()
            .addOnSuccessListener { _ -> Log.d("Success Delete Product", "Successfully Deleted Product with id ${productId}") }
            .addOnFailureListener { _ -> Log.d("Fail Delete Product", "Failed to Delete Product with id ${productId}") }
    }

    fun modifyProductInDB(productId: String, productPrice: String, productTitle: String, productDescription: String, productQuantity: String){

        db.collection("products").document(productId)
            .update(mapOf(
                "price" to productPrice,
                "title" to productTitle,
                "description" to productDescription,
                "stock_quantity" to productQuantity,
                ))
            .addOnSuccessListener { _ -> Log.d("Success Modify Product", "Successfully Modified Product with id ${productId}")  }
            .addOnFailureListener { e -> Log.w("Fail Modify Product", "Failed to Modify Product with id ${productId}", e) }
    }

    fun getProductsInAscending(onSuccess: (List<Product>) -> Unit, onFailure: (Exception) -> Unit) {
        val query = db.collection("products")
            .orderBy("product_id", Query.Direction.ASCENDING)
            .limit(100)

        query.get()
            .addOnSuccessListener { documents ->
                val productList = mutableListOf<Product>()
                for (document in documents) {
                    val product = document.toObject(Product::class.java)
                    productList.add(product)
                }
                onSuccess(productList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getUserID(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = "1"
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID

    }

    fun uploadProductDetails(productInfo: Product, response: (isSuccess: Boolean) -> Unit) {
        db.collection("products")
            .add(productInfo) // Add the productInfo directly and let Firestore generate the document ID
            .addOnSuccessListener { documentReference ->
                // Retrieve the generated document ID
                val productId = documentReference.id
                // Update the productInfo with the generated document ID
                val updatedProductInfo = productInfo.copy(product_id = productId)

                //Retrieve the user's id

                // Update the document with the new productInfo including the document ID
                db.collection("products")
                    .document(productId)
                    .set(updatedProductInfo, SetOptions.merge())
                    .addOnSuccessListener {
                        response(true)
                    }
                    .addOnFailureListener {
                        response(false)
                    }

            }
            .addOnFailureListener {
                response(false)
            }
    }

    fun getProductList(updateProductList: (input: ArrayList<Product>) -> Unit) {
        db.collection("products")
            .whereEqualTo("user_id", getUserID())
            .get()
            .addOnSuccessListener { document ->
                val productList: ArrayList<Product> = ArrayList()
                for (p in document.documents) {
                    val product = p.toObject(Product::class.java)
                    product!!.product_id = p.id
                    productList.add(product)
                }
                updateProductList(productList)
            }
    }

    fun addItemToCart(addToCart: Cart, response: (isSuccess: Boolean) -> Unit) {
        db.collection("cart_items")
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                response(true)
            }.addOnFailureListener {
                response(false)
            }
    }

//    fun getAllProductsList(activity: Activity) {
//        db.collection("products")
//            .get()
//            .addOnSuccessListener { document ->
//
//                val productsList: ArrayList<Product> = ArrayList()
//                for (i in document.documents) {
//
//                    val product = i.toObject(Product::class.java)
//                    product!!.product_id = i.id
//
//                    productsList.add(product)
//                }
//
//                when (activity) {
//                    is CartActivity -> {
//                        activity.successProductListFromFS(productsList)
//                    }
//                }
//            }
//            .addOnFailureListener { e ->
//
//            }
//    }

    fun getCartList(updateProductList: (input: ArrayList<Cart>) -> Unit) {
        db.collection("cart_items")
            .whereEqualTo("user_id", getUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<Cart> = ArrayList()
                for (cart in document.documents) {
                    val cartItem = cart.toObject(Cart::class.java)
                    cartItem!!.id = cart.id
                    list.add(cartItem)
                }
                updateProductList( list)
            }.addOnFailureListener {
            }


    }

    fun getWishList(updatesList: (input: ArrayList<WishListModel>) -> Unit) {
        db.collection("wish_list")
            .whereEqualTo("user_id", getUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<WishListModel> = ArrayList()
                for (cart in document.documents) {
                    val cartItem = cart.toObject(WishListModel::class.java)
                    cartItem!!.id = cart.id
                    list.add(cartItem)
                }
                updatesList(list)
            }.addOnFailureListener {}
    }

    fun getChatList(userId: String, updatesList: (input: ArrayList<ChatActivity.Chat>) -> Unit) {
        db.collection("chats").whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { document ->
                Log.e("TAgsa","This is size document ${document.documents.size}")
                val list: ArrayList<ChatActivity.Chat> = ArrayList()
                for (cart in document.documents) {
                    val cartItem = cart.toObject(ChatActivity.Chat::class.java)
                    cartItem!!.id = cart.id
                    list.add(cartItem)
                }
                updatesList(list)
            }
            .addOnFailureListener { exception ->
                Log.e("TAGSA","This is the documents ${exception}")
//                onFailure(exception)
            }

    }

    fun addItemInWishList(addToWishList: WishListModel, response: (isSuccess: Boolean) -> Unit) {

        db.collection("wish_list")
            .document()
            .set(addToWishList, SetOptions.merge())
            .addOnSuccessListener {
                response(true)
            }.addOnFailureListener {
                response(false)
            }
    }

    fun removeItemInWishList(
        context: Context,
        wishId: String,
        response: (isSuccess: Boolean) -> Unit
    ) {
        db.collection("wish_list")
            .document(wishId)
            .delete()
            .addOnSuccessListener {
                response(true)
            }.addOnFailureListener {
                response(false)
            }
    }

    fun updateCartList(
        context: Context,
        cartId: String,
        itemHashMap: HashMap<String, Any>,
        response: (isSuccess: Boolean) -> Unit
    ) {
        db.collection("cart_items")
            .document(cartId)
            .update(itemHashMap)
            .addOnSuccessListener {
                response(true)
            }
            .addOnFailureListener {
                response(false)
            }

    }

    fun removeItemInCart(cartId: String,response: (isSuccess:Boolean) -> Unit){
        db.collection("cart_items")
            .document(cartId)
            .delete()
            .addOnSuccessListener {
                response(true)
            }.addOnFailureListener {
                response(false)
            }
    }

    // Function to create an address
    fun createAddress(address: Address, response: (isSuccess:Boolean) -> Unit) {
        db.collection("addresses")
            .add(address)
            .addOnSuccessListener { documentReference ->
                response(true)
            }
            .addOnFailureListener { e ->
                response(false)
            }
    }


    // Function to create an address
    fun deleteAddress(addressId: String, response: (isSuccess:Boolean) -> Unit) {
        db.collection("addresses")
            .document(addressId)
            .delete()
            .addOnSuccessListener {
                response(true)
            }.addOnFailureListener {
                response(false)
            }
    }

    fun getAddressList( updatesList: (input: ArrayList<Address>) -> Unit){
        db.collection("addresses")
            .whereEqualTo("userId",getUserID())
            .get()
            .addOnSuccessListener {document->
                val list: ArrayList<Address> = ArrayList()
                for (cart in document.documents){
                    val cartItem = cart.toObject(Address::class.java)
                    cartItem!!.id = cart.id
                    list.add(cartItem)
                }
                updatesList(list)
            }.addOnFailureListener {

            }

    }

    /*

    fun createOrder(activity: CheckoutActivity, order: Order){
        db.collection("orders")
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.orderCreatedSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressBar()
            }
    }




    fun updateProductCartDetails(activity: CheckoutActivity, cartList: ArrayList<Cart>, order: Order){
        val write = db.batch()

        for (cart in cartList) {

            val soldProduct = SoldProduct(
                cart.product_owner_id,
                cart.title,
                cart.price,
                cart.cart_quantity,
                cart.image,
                order.title,
                order.order_date,
                order.sub_total_amount,
                order.shipping_charge,
                order.total_amount,
                order.address
            )

            val documentReference = db.collection("sold_products")
                .document(cart.product_id)

            write.set(documentReference, soldProduct)
        }

        for (cart in cartList) {

            val documentReference = db.collection("cart_items")
                .document(cart.id)
            write.delete(documentReference)
        }

        write.commit().addOnSuccessListener {

            activity.cartDetailsUpdatedSuccessfully()

        }.addOnFailureListener {
            activity.hideProgressBar()
        }

    }

    fun getSoldProductsList(activity: SoldProductsActivity){
        db.collection("sold_products")
            .whereEqualTo("user_id",getUserID())
            .get()
            .addOnSuccessListener {document->
                val list: ArrayList<SoldProduct> = ArrayList()
                for(i in document.documents){
                    val soldProduct = i.toObject(SoldProduct::class.java)!!
                    soldProduct.id = i.id
                    list.add(soldProduct)
                }
                activity.successSoldProductList(list)

            }
            .addOnFailureListener {
                println("error while getting sold products")
                it.printStackTrace()
                activity.hideProgressBar()
            }
    }


@Parcelize
data class Order(
    val user_id: String = "",
    val items: ArrayList<Cart> = ArrayList(),
    val address: Address = Address(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    var order_date: Long = 0L,
    var id: String = "",
) : Parcelable



@Parcelize
data class SoldProduct(
    val user_id: String = "",
    val title: String = "",
    val price: String = "",
    val sold_quantity: String = "",
    val image: String = "",
    val order_id: String = "",
    val order_date: Long = 0L,
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val address: Address = Address(),
    var id: String = "",
) : Parcelable




     */
}