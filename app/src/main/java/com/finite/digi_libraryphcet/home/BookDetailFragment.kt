package com.finite.digi_libraryphcet.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.finite.digi_libraryphcet.R
import com.finite.digi_libraryphcet.adapter.HorizBookAdapter
import com.finite.digi_libraryphcet.databinding.FragmentBookDetailBinding
import com.finite.digi_libraryphcet.model.BookModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class BookDetailFragment : Fragment() {

    val args: BookDetailFragmentArgs by navArgs()
    private var binding : FragmentBookDetailBinding? = null
    private lateinit var dbref : DatabaseReference
    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var dbref3 : DatabaseReference
    private lateinit var mAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookCode = args.bookCode
        viewModel.scancode = "default"
        mAuth = FirebaseAuth.getInstance()
        var currentBookName = ""
        var currentPicUrl = ""
        var currentAuthor = ""
        var bookName = ""
        var authorName = ""
        var noBooks = 0


        dbref = FirebaseDatabase.getInstance().getReference("books/all/$bookCode")
        dbref3 = FirebaseDatabase.getInstance().getReference("pendingBooks/$bookCode")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding!!.bookTitle.text = "${snapshot.child("bookName").value.toString()}"
                    binding!!.bookAuthor.text = "By ${snapshot.child("author").value.toString()}"
                    binding!!.bookDesc.text = "Description : ${snapshot.child("desc").value.toString()}"
                    binding!!.bookShelf.text = "Shelf No. : ${snapshot.child("shelfNo").value.toString()}"
                    binding!!.bookNoOfCopies.text = "Available : ${snapshot.child("noCopies").value.toString()} Copies"

                    currentBookName = snapshot.child("bookName").value.toString()
                    currentPicUrl = snapshot.child("picurl").value.toString()
                    currentAuthor = snapshot.child("author").value.toString()
                    noBooks = Integer.parseInt(snapshot.child("noCopies").value.toString())

                    Glide.with(this@BookDetailFragment).load(snapshot.child("picurl").value.toString()).centerCrop().into(binding!!.bookImage)

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
            if(noBooks < 1) {
                Toast.makeText(context, "No copies available currently!", Toast.LENGTH_LONG).show()
            }

            else {
                Toast.makeText(context, "Request pending, visit library to confirm issue", Toast.LENGTH_LONG).show()

                mAuth = FirebaseAuth.getInstance()
                val currentUser = mAuth.currentUser
                dbref3.child("reqBy").setValue(currentUser!!.uid)
                dbref3.child("reqName").setValue(currentUser!!.displayName)
                dbref3.child("bookName").setValue(currentBookName)
                dbref3.child("noCopies").setValue(noBooks.toString())
                dbref3.child("bookCode").setValue(bookCode)
                dbref3.child("bookAuthor").setValue(currentAuthor)
                dbref3.child("picurl").setValue(currentPicUrl)

            }
        }

    }

    fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }


}