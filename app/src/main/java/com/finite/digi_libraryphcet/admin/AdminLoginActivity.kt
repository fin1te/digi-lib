package com.finite.digi_libraryphcet.admin

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.finite.digi_libraryphcet.adapter.PendingBookAdapter
import com.finite.digi_libraryphcet.databinding.ActivityAdminLoginBinding
import com.finite.digi_libraryphcet.model.PendingModel
import com.google.firebase.database.*


class AdminLoginActivity : AppCompatActivity() {

    private lateinit var pendingArrayList : ArrayList<PendingModel>
    private lateinit var binding: ActivityAdminLoginBinding
    private lateinit var pendingBookrecview : RecyclerView
    private lateinit var dbref4 : DatabaseReference
    private lateinit var adapter: PendingBookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**COMP BOOKS*/
        pendingBookrecview = binding!!.pendingRecView
        pendingBookrecview.setHasFixedSize(true)
        pendingArrayList =  arrayListOf()
        getPendingBookData()

//        PendingBookAdapter.onItemClick = {
//
//            Toast.makeText(applicationContext, "Testing", Toast.LENGTH_SHORT).show()
//        }

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
                    adapter = PendingBookAdapter(pendingArrayList)
                    pendingBookrecview.adapter = adapter
                    adapter.onItemClick = {
                        val mBuilder = AlertDialog.Builder(this@AdminLoginActivity)
                            .setTitle("Confirm")
                            .setMessage("Are you sure you want to exit?")
                            .setPositiveButton("Yes", null)
                            .setNegativeButton("No", null)
                            .show()

                        // Function for the positive button
                        // is programmed to exit the application
                        val mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
                        mPositiveButton.setOnClickListener {
                        }

                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
}