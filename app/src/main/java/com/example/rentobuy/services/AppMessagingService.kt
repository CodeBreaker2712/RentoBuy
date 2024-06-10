package com.example.rentobuy.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.telecom.Call
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.rentobuy.MainActivity
import com.example.rentobuy.R
import com.example.rentobuy.modules.chat.ChatListActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging
import java.io.IOException
import okhttp3.*

class AppMessagingService : FirebaseMessagingService(){

    init {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("TAG", "This is token $token")
        })

        Firebase.messaging.subscribeToTopic("user1")
            .addOnCompleteListener { task ->
                var msg = "This is tokenSubscribed"
                if (!task.isSuccessful) {
                    msg = "This is tokenSubscribe failed"
                }
                Log.d("TAG", msg)
            }
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d("TAG", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
//        sendRegistrationToServer(token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("TAG", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("TAG", "Message data payload: ${remoteMessage.data}")
            sendNotification(remoteMessage.notification?.body?:"")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("TAG", "Message Notification Body: ${it.body}")
        }
    }

   private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val requestCode = 0
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val channelId = "General"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("RentoBuy")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "General",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    fun sendNotificationFromActivity(context:Context, messageBody: String, senderId: String) {
        val intent = Intent(context.applicationContext, ChatListActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val requestCode = 0
        val pendingIntent = PendingIntent.getActivity(
            context.applicationContext,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val channelId = "General"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context.applicationContext, channelId)
            .setContentTitle(senderId)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setSmallIcon(R.drawable.icon)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "General",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

}

class SendUserNotificationUtil{

//    fun sendNotificationToUser(){
//
//        val topic = "highScores"
//
//        val message: RemoteMessage = RemoteMessage() .builder()
//            .putData("score", "850")
//            .putData("time", "2:45")
//            .setTopic(topic)
//            .build()
//
//        FirebaseMessaging.getInstance().send(message)
//    }
}
//
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.messaging.FirebaseMessaging
//import com.google.firebase.messaging.Message
//import com.google.firebase.messaging.Notification
//
//// User data class representing the structure of user data
//data class User(
//    val userId: String = "",
//    val userName: String = "",
//    val notificationToken: String = ""
//)
//
//// Function to store user data in the "notifications" collection
//fun saveUserData(userId: String, userName: String, notificationToken: String) {
//    val db = FirebaseFirestore.getInstance()
//    val user = User(userId, userName, notificationToken)
//
//    // Add the user to the "notifications" collection
//    db.collection("notifications").document(userId)
//        .set(user)
//        .addOnSuccessListener {
//            // User data saved successfully
//        }
//        .addOnFailureListener { e ->
//            // Handle error
//        }
//}
//
//// Function to send notifications using FCM
//fun sendNotification(userId: String, notificationMessage: String) {
//    val db = FirebaseFirestore.getInstance()
//
//    // Get the user's token from the "notifications" collection
//    val userRef = db.collection("notifications").document(userId)
//    userRef.get()
//        .addOnSuccessListener { documentSnapshot ->
//            if (documentSnapshot.exists()) {
//                val user = documentSnapshot.toObject(User::class.java)
//                if (user != null && user.notificationToken.isNotBlank()) {
//                    val message = RemoteMessage.Builder( user.notificationToken)
//                        .setData(
//                            mapOf(
//                                "title" to "Your notification title",
//                                "body" to "Your notification body",
//                                "custom_key" to "Custom value"
//                            )
//                        )
//                        .build()
//
//                    FirebaseMessaging.getInstance().send(message)
//                }
//            }
//        }
//        .addOnFailureListener { e ->
//            // Handle error
//        }
//}
//
//
//object AccessTokenFetcher {
//
//    fun fetchAccessToken() {
//
//        val credentials = """
//    {
//        "private_key_id": "2d0d58e95fd492c9bbb029c6c2bd72a4e1b80b43",
//       "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDONoVQbgsYmHVn\ndVoj7rnHAGGN1CMJBsGDV2C4nFa2riiAkzu9OXKavDFS6kEwa/87LhxOU8bSRbKy\nx+VTDnkJmARMecwh/2sjqK0IMNiBVGRO5ScWFArckXAGgYWw2FPg6AoZ6ZzpJ+O4\npK6GGL+eeoa3fVYwGwE/znsUL2yxX9cvb0MsvjI+BiFgkD2uVK74mw8PFff0cmFM\nWB+Sjz6ldOAnOzm/uOq5zVTylIw9r2Tc8ijwt3xv87Qdrvg8M8DRjNIemzJ0pAoq\ni9q8ByttaQYxjxW4viL4xG1NU9rwIaSbjkbgUZMvgYE6pkS/h2POXbcyvQR9xMZV\n0tXjw+8JAgMBAAECggEAK3IKKVVd6hso1RjA2oQvyND4PKjejsbJG3Jb0tlBXbCY\noOokMmw5jIcsvyLShDCkr+O5m/kDkQaVLRx3YMXSU2afNTuN+fW1rWgauHiODMVq\ncsLNttWXKjxMBVrhf048ccyBYE87O1YnoJ21lVP+fxWib2MtNuphm39Vp/2L4VyG\nD8+tW/jkfodGMEbUrQe4Mx79nPoOxi2gsV7TO/pIiEoIyfD8JIHVWGRfFLJ6sNqI\n0CjJon0GB36lrZgR3tkxNmE+JZy25ov9HwPsc1VJraQ/AwhQS4X7K/FbzHZnGChB\n6BHLPS45hc4l+Rxpz6tFn5rXWxubKc3xV9VWkRNe+QKBgQD9rjr6MkCpc8O53ld3\n1/Hcy20YYiB2oc105S82R0IBTD3mKaaUUAsu/3/eK6KvNEidUs3q1R9si5Qen9qC\nUqbADA3A0A/NMESypJ6ZVX0x8XMyBKmZKdodXjq5U632MUM27LWAarAeQ/l5baX1\n175S4HC4CvuReHT6G/KTOrVxnQKBgQDQGS/QSQ079+bIHSDnEJsIMDd4eFQFTzAv\nRRTui3h3i9a4p1yPKOjQ2cMWGtvPt+biCjWQOn0yRB+PVODYrgsdiTP33Mp62lJs\n2qW+rMj5t6AfL4hN8NOZvE8jTkqnaXkvoXKLr6UiHfCyUHc9GHi7vMZxMMRiCgdB\nNmMTZuB9XQKBgQCCYk6F0dZSQeHTYmfqfN8e8J997NFqp/cFodYGO9G9AQa/iaDI\nkettmHyMIDlZe7Hmymxzyl6AV60loFuaqAh0pL1c4JN+jtSATQQHI11ZkhP3HVZe\nFpi4h3n/TvcWlnNJg5g2DJ/ArodG1gh6twxnZFwK+/oY5qSY+ExRR9eCMQKBgG0s\nKYduK8FARxTJI0/VyF+9W/yZtAqtBqxl45taFDMza8yoEMkSP0ICXHSmVpdc9YW0\nsXEfHjtWve/mKafDdxVhu5/Xd2qISyHMe5PS9gT8Lsbtmua452oJ/sKY+lVl4iv4\n2S0rUsjZSENkL4of4ocrfDg5y21bsX6m18d/+1oJAoGAV14HJ3avjlybsEWII/qH\n8VKnU3yimlMbkmN7qWtYIHEzdUJeb8u/D39nRJDfG1777BoRkpEqI/QJgkRAZBUG\n2HB8X2c/UVwxBZjx5QYstsCfQMUUv7wBju9qPmBAQf5LUs8ay2GewW+xwf2y+TIL\ni98+Z1EeiSloTL/g3cXCcXU=\n-----END PRIVATE KEY-----\n",
//  "client_email": "firebase-adminsdk-d8see@rentobuy-abe7e.iam.gserviceaccount.com",
//        "token_uri": "https://oauth2.googleapis.com/token"
//    }
//""".trimIndent()
//
//        val client = OkHttpClient()
//
//        val requestBody = FormBody.Builder()
//            .add("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
//            .add("assertion", credentials)
//            .build()
//
//        val request = Request.Builder()
//            .url("https://oauth2.googleapis.com/token")
//            .post(requestBody)
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//
//            override fun onFailure(call: okhttp3.Call, e: IOException) {
//                e.printStackTrace()
//
//            }
//
//            override fun onResponse(call: okhttp3.Call, response: Response) {
//                if (!response.isSuccessful) {
//                    throw IOException("Unexpected code $response")
//                } else {
//                    val responseBody = response.body?.string()
//                    // Parse responseBody to extract access token
//                    println("Access Token: $responseBody")
//                    Log.e("TAGSAA","TOKEN IS $responseBody");
//                }
//            }
//        })
//    }
//}
