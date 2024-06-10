package com.example.rentobuy.modules.delivery

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.R
import com.example.rentobuy.adapter.AddressAdapter
import com.example.rentobuy.adapter.WishListAdapter
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.Address
import com.example.rentobuy.model.WishListModel
import com.example.rentobuy.modules.payment.PaymentActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.play.integrity.internal.ad

class AddressSelectionActivity : AppCompatActivity(), AddressAdapter.OnItemClickListener {

    lateinit var editTextAddressLabel: TextInputEditText
    lateinit var btn_add_address: MaterialButton
    lateinit var btn_place_picker: MaterialButton

    var addressValue: String = ""
    var addressName: String = ""
    var lat: Double = 0.0
    var long: Double = 0.0
    var zipCode: String = ""


    var addressList: ArrayList<Address> = arrayListOf()
    lateinit var rvAddress: RecyclerView

    lateinit var addressAdapter: AddressAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_address_selection)

        rvAddress = findViewById(R.id.rvAddress)
        editTextAddressLabel = findViewById(R.id.editTextAddressLabel)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar?.title = "Choose Address"

        btn_add_address = findViewById(R.id.btn_add_address)
        btn_add_address.setOnClickListener {
            hideKeyboard()
            val address = Address(
                Database().getUserID(),
                editTextAddressLabel.text?.trim().toString() ?: "",
                addressValue,
                "B3k 2Z2",
                lat,
                long,
                addressName
            )
            Database().createAddress(address, response = {
                initView()
                if (it) {
                    Toast.makeText(this, "Address updated.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Address error.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        val startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.e("TAG", "Place: ${place.name}, ${place.latLng}")
                addressValue = place.address?:""
                lat = place.latLng?.latitude ?: 0.0
                long = place.latLng?.longitude ?: 0.0
                addressName = place.name?:""
            } else if (result.resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(result.data!!)
                Log.e("TAG", status.statusMessage ?: "")
            }
        }

        btn_place_picker = findViewById(R.id.btn_place_picker)
        btn_place_picker.setOnClickListener {

            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)

            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)

            startForResult.launch(intent)

        }

        initView()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun initView() {

        Places.initialize(applicationContext, "AIzaSyCgP3-I-Il2_u-2cfazwGpkCU11dNUDKGY")

        Database().getAddressList {
            addressList = it
            if (addressList.size > 0) {
                rvAddress.visibility = View.VISIBLE
                rvAddress.layoutManager = LinearLayoutManager(this)
                rvAddress.setHasFixedSize(true)
                addressAdapter = AddressAdapter(addressList, this)
                rvAddress.adapter = addressAdapter
            } else {
                rvAddress.visibility = View.GONE
            }

        }
    }

    override fun onDeleteAddress(item: Address) {
        Database().deleteAddress( item.id, response = {
            if (it) {
                initView()
                Toast.makeText(this, "Address removed.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Address error.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSelectAddress(item: Address) {
        startActivity(Intent(this, PaymentActivity::class.java))
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

}