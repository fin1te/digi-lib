package com.finite.digi_libraryphcet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

//    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("testlog", "ma onCreate: Called")

//        mAuth = FirebaseAuth.getInstance()
//        val user = mAuth.currentUser

//        runOnUiThread {
//            MainScope().launch {
//                delay(2000)
//                /** Login Activity redirect */
//                Log.d("testlog", "reached intent")
//                val loginIntent = Intent(this@MainActivity, LoginActivity::class.java)
//                startActivity(loginIntent)
//
////                val homeIntent = Intent(this@MainActivity, HomeActivity::class.java)
////                startActivity(loginIntent)
//            }
//        }

        val loginIntent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }
}