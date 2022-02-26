package com.finite.digi_libraryphcet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.finite.digi_libraryphcet.databinding.ActivityHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding
    //private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

//        id_txt.text = currentUser?.uid
//        name_txt.text = currentUser?.displayName
//        email_txt.text = currentUser?.email
//
//        Glide.with(this).load(currentUser?.photoUrl).into(profile_image)

        binding.signOutBtn.setOnClickListener {
            mAuth.signOut()
            //googleSignInClient.revokeAccess()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}