package com.finite.digi_libraryphcet.admin

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
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
    private lateinit var dbref5 : DatabaseReference
    private lateinit var dbref6 : DatabaseReference
    private lateinit var dbref7 : DatabaseReference
    private lateinit var dbref8 : DatabaseReference
    private lateinit var dbref9 : DatabaseReference

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
        var currentBooks = 0
        dbref4.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                pendingArrayList.removeAll(pendingArrayList)
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){

                        val book = userSnapshot.getValue(PendingModel::class.java)
                        pendingArrayList.add(book!!)

                    }
                    adapter = PendingBookAdapter(pendingArrayList)
                    adapter.notifyDataSetChanged()
                    pendingBookrecview.adapter = adapter
                    adapter.onItemClick = { pendingModel ->
                        val mBuilder = AlertDialog.Builder(this@AdminLoginActivity)
                            .setTitle("Book Issue Request")
                            .setMessage("Accept the issue request from ${pendingModel.reqName}?")
                            .setPositiveButton("Accept", null)
                            .setNeutralButton("Decline", null)
                            .show()

                        // Function for the positive button
                        // is programmed to exit the application
                        var mPositiveButton = mBuilder.getButton(AlertDialog.BUTTON_POSITIVE)
                        var mNegativeButton = mBuilder.getButton(AlertDialog.BUTTON_NEUTRAL)

                        mNegativeButton.setOnClickListener {
                            Toast.makeText(
                                this@AdminLoginActivity,
                                "Issue request of ${pendingModel.reqName} is denied!",
                                Toast.LENGTH_SHORT
                            ).show()

                            dbref8 =  FirebaseDatabase.getInstance().getReference("pendingBooks/${pendingModel.bookCode}")
                            dbref8.child("bookAuthor").removeValue()
                            dbref8.child("bookCode").removeValue()
                            dbref8.child("bookName").removeValue()
                            dbref8.child("noCopies").removeValue()
                            dbref8.child("picurl").removeValue()
                            dbref8.child("reqBy").removeValue()
                            dbref8.child("reqName").removeValue()

                            mBuilder.dismiss()
                        }

                        mPositiveButton.setOnClickListener {
                            dbref5 = FirebaseDatabase.getInstance().getReference("user/${pendingModel.reqBy}")



                            dbref5.addListenerForSingleValueEvent(object: ValueEventListener {

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    currentBooks = Integer.parseInt(snapshot.child("booksIssue").value.toString())
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })

                            currentBooks++

                            if(currentBooks > 4) {
                                Toast.makeText(
                                    this@AdminLoginActivity,
                                    "Maximum books issue for ${pendingModel.reqName}!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                                dbref7 =  FirebaseDatabase.getInstance().getReference("pendingBooks/${pendingModel.bookCode}")
                                dbref7.child("bookAuthor").removeValue()
                                dbref7.child("bookCode").removeValue()
                                dbref7.child("bookName").removeValue()
                                dbref7.child("noCopies").removeValue()
                                dbref7.child("picurl").removeValue()
                                dbref7.child("reqBy").removeValue()
                                dbref7.child("reqName").removeValue()

                                mBuilder.dismiss()
                            } else {

                                Toast.makeText(
                                    this@AdminLoginActivity,
                                    "Book issued to ${pendingModel.reqName}!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                                dbref6 = FirebaseDatabase.getInstance().getReference("user/${pendingModel.reqBy}")
                                dbref6.child("booksIssue").setValue(currentBooks.toString())

                                dbref6 =  FirebaseDatabase.getInstance().getReference("pendingBooks/${pendingModel.bookCode}")
                                dbref6.child("bookAuthor").removeValue()
                                dbref6.child("bookCode").removeValue()
                                dbref6.child("bookName").removeValue()
                                dbref6.child("noCopies").removeValue()
                                dbref6.child("picurl").removeValue()
                                dbref6.child("reqBy").removeValue()
                                dbref6.child("reqName").removeValue()


                                mBuilder.dismiss()
                            }



                        }

//                        mNegativeButton.setOnClickListener {
//
//                        }

                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
}