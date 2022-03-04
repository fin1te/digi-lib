package com.finite.digi_libraryphcet

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.finite.digi_libraryphcet.databinding.ActivityDetailBinding
import com.finite.digi_libraryphcet.databinding.ActivityScanBinding
import com.finite.digi_libraryphcet.home.SharedViewModel
import com.google.firebase.database.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private lateinit var dbref : DatabaseReference
    private lateinit var dbref2 : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var bookCode = ""
        var bookName = ""
        var authorName = ""

//        dbref = FirebaseDatabase.getInstance().getReference("currentCode")
//        dbref.addValueEventListener(object : ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    bookCode = snapshot.value.toString()
//
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })

        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        bookCode = sharedPreference.getString("currentCode","HE008954").toString()

        dbref2 = FirebaseDatabase.getInstance().getReference("books/all/$bookCode")
        //Toast.makeText(this@DetailActivity, dbref2.toString(), Toast.LENGTH_LONG).show()
        Log.d("testlog", dbref2.toString())
        dbref2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.bookTitle.text = "${snapshot.child("bookName").value.toString()}"
                    binding.bookAuthor.text = "By ${snapshot.child("author").value.toString()}"
                    binding.bookDesc.text = "Description : ${snapshot.child("desc").value.toString()}"
                    binding.bookShelf.text = "Shelf No. : ${snapshot.child("shelfNo").value.toString()}"
                    binding.bookNoOfCopies.text = "Available : ${snapshot.child("noCopies").value.toString()} Copies"
                    Glide.with(this@DetailActivity).load(snapshot.child("picurl").value.toString()).centerCrop().into(binding!!.bookImage)

                    bookName = snapshot.child("bookName").value.toString()
                    authorName = snapshot.child("author").value.toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

        binding!!.searchNetBtn.setOnClickListener {
            openLink("https://www.google.com/search?q=$bookName $authorName")
        }

        binding!!.issueBookBtn.setOnClickListener {
            Toast.makeText(this, "Under Development!", Toast.LENGTH_SHORT).show()
        }

    }
    fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }
}