package com.finite.digi_libraryphcet.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.finite.digi_libraryphcet.R
import com.finite.digi_libraryphcet.databinding.FragmentProfileBinding
import com.finite.digi_libraryphcet.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class ProfileFragment : Fragment() {

    private var binding : FragmentProfileBinding? = null
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val database = Firebase.database
        val myRef = database.getReference("user")

        try {
            myRef.child(currentUser!!.uid).addValueEventListener(object:
                ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val data = snapshot.getValue(UserModel::class.java)
                    Glide.with(requireContext()).load(data?.picurl).centerCrop().into(binding!!.photoUrl)
                    binding!!.name.text = data?.uname
                    binding!!.email.text = data?.email
                    if(!data?.type.isNullOrBlank()) {
                        binding!!.type.text = data?.type
                    }
                    if(!data?.booksIssue.isNullOrBlank()) {
                        binding!!.booksIssue.text = data?.booksIssue
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("testModel", "Failed to read value.", error.toException())
                }
            })
        }
        catch (e: Exception) {
            Log.d("testlog", e.toString())
        }



//        currentUser?.displayName
//        currentUser?.phoneNumber
//        currentUser?.email
//        currentUser?.uid



    }


}