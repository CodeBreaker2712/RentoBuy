package com.example.rentobuy.modules.notification

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.ImageButton
import android.widget.Switch
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.rentobuy.R
import com.example.rentobuy.modules.home.UserProfileActivity

class NotificationSettingsActivity : AppCompatActivity() {
    private lateinit var switchEnableNotifications: SwitchCompat;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        switchEnableNotifications = findViewById(R.id.switchEnableNotifications)

        switchEnableNotifications.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                askNotificationPermission()
            } else {
                // Switch is unchecked, disable notifications
                // textEnableNotifications.text = "Notifications Disabled"
                // Call a function to handle disabling notifications
                // disableNotifications()
                openAppSettings(this)
            }
        }

        btnBack.setOnClickListener{
            onBack()
        }
    }

    override fun onStart() {
        super.onStart()

        val notificationsEnabled = areNotificationsEnabled(applicationContext)
        switchEnableNotifications.isChecked = notificationsEnabled
    }

    fun areNotificationsEnabled(context: Context): Boolean {
        val notificationManager = NotificationManagerCompat.from(context)
        return notificationManager.areNotificationsEnabled()
    }

    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            switchEnableNotifications.isChecked = true;
            // FCM SDK (and your app) can post notifications.
        } else {
            switchEnableNotifications.isChecked = false;
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun onBack() {
        // Navigate to ProductsActivity when back button is pressed
        startActivity(Intent(this, UserProfileActivity::class.java))
        finish()
    }
}