package com.finite.digi_libraryphcet.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.finite.digi_libraryphcet.R
import com.finite.digi_libraryphcet.adapter.HorizBookAdapter
import com.finite.digi_libraryphcet.adapter.PendingBookAdapter
import com.finite.digi_libraryphcet.databinding.ActivityAdminLoginBinding
import com.finite.digi_libraryphcet.databinding.ActivityLoginBinding
import com.finite.digi_libraryphcet.model.BookModel
import com.finite.digi_libraryphcet.model.PendingModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.*

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var pendingArrayList : ArrayList<PendingModel>
    private lateinit var binding: ActivityAdminLoginBinding
    private lateinit var pendingBookrecview : RecyclerView
    private lateinit var dbref4 : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**COMP BOOKS*/
        pendingBookrecview = binding!!.pendingRecView
        pendingBookrecview.setHasFixedSize(true)
        pendingArrayList =  arrayListOf()
        getPendingBookData()


    }

    private fun getPendingBookData() {

        dbref4 = FirebaseDatabase.getInstance().getReference("pendingBooks")
        dbref4.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){

                        val book = userSnapshot.getValue(PendingModel::class.java)
                        pendingArrayList.add(book!!)

                    }
                    pendingBookrecview.adapter = PendingBookAdapter(pendingArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
}