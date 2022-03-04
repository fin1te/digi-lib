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
import com.finite.digi_libraryphcet.adapter.HorizBookAdapter
import com.finite.digi_libraryphcet.databinding.FragmentHomeBinding
import com.finite.digi_libraryphcet.model.BookModel
import com.google.firebase.database.*


class HomeFragment : Fragment() {

    var binding : FragmentHomeBinding? = null
    private lateinit var feBookrecview : RecyclerView
    private lateinit var compBookrecview : RecyclerView
    private lateinit var bookArrayList : ArrayList<BookModel>
    private lateinit var CompBookArrayList : ArrayList<BookModel>
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

        /**FE BOOKS*/
        feBookrecview = binding!!.feBookrecview
//        bookRecView.layoutManager = LinearLayoutManager(requireContext())
        feBookrecview.setHasFixedSize(true)
        bookArrayList =  arrayListOf()
        getFEBookData()

        /**COMP BOOKS*/
        compBookrecview = binding!!.compBookRecView
        compBookrecview.setHasFixedSize(true)
        CompBookArrayList =  arrayListOf()
        getCompBookData()
    }

    private fun getFEBookData() {

        dbref = FirebaseDatabase.getInstance().getReference("books/fe")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){

                        val user = userSnapshot.getValue(BookModel::class.java)
                        bookArrayList.add(user!!)

                    }
                    feBookrecview.adapter = HorizBookAdapter(bookArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }

    private fun getCompBookData() {

        dbref = FirebaseDatabase.getInstance().getReference("books/comp")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){

                        val user = userSnapshot.getValue(BookModel::class.java)
                        CompBookArrayList.add(user!!)

                    }
                    compBookrecview.adapter = HorizBookAdapter(CompBookArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
}