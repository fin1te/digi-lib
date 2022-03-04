package com.finite.digi_libraryphcet.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.finite.digi_libraryphcet.adapter.BookAdapter
import com.finite.digi_libraryphcet.databinding.FragmentHomeBinding
import com.finite.digi_libraryphcet.model.BookModel
import com.google.firebase.database.*


class HomeFragment : Fragment() {

    var binding : FragmentHomeBinding? = null
    private lateinit var bookRecView : RecyclerView
    private lateinit var bookArrayList : ArrayList<BookModel>
    private lateinit var dbref : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookRecView = binding!!.bookrecview
//        bookRecView.layoutManager = LinearLayoutManager(requireContext())
        bookRecView.setHasFixedSize(true)
        bookArrayList =  arrayListOf()

        getBookData()
    }

    private fun getBookData() {

        dbref = FirebaseDatabase.getInstance().getReference("books")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){

                        val user = userSnapshot.getValue(BookModel::class.java)
                        bookArrayList.add(user!!)

                    }
                    bookRecView.adapter = BookAdapter(bookArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
}