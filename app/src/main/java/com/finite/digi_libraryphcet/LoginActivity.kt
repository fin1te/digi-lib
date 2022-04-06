package com.finite.digi_libraryphcet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.finite.digi_libraryphcet.admin.AdminLoginActivity
import com.finite.digi_libraryphcet.databinding.ActivityLoginBinding
import com.finite.digi_libraryphcet.home.HomeActivity
import com.finite.digi_libraryphcet.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("testlog", "onCreate: Called")

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("132996224253-qscc9qijdvg8bvri8nfmmiu37apq5qoc.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //Firebase Auth instance
        mAuth = FirebaseAuth.getInstance()

        binding.signInGoogle.setOnClickListener {
            googleSignInClient.revokeAccess()
            Log.d("testlog", "Reached signIn()")
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInClient.revokeAccess()
        Log.d("testlog", "reached startActivityForResult")
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("SignInActivity", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        val database = Firebase.database
        val myRef = database.getReference("user")

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    mAuth = FirebaseAuth.getInstance()
                    val currentUser = mAuth.currentUser
                    if(currentUser!!.email!!.contains("@mes.ac.in") || currentUser.email!!.contains("@student.mes.ac.in") ) {
                        // Sign in success, add user's data to firebase
                        myRef.child(currentUser.uid).addListenerForSingleValueEvent(object:
                            ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                if(snapshot.value == null) {
                                    val userModel = UserModel(uname = currentUser.displayName!!, email = currentUser.email!!,type = "student", uid = currentUser.uid, picurl = currentUser.photoUrl.toString())
                                    myRef.child(currentUser.uid).setValue(userModel)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.w("testModel", "Failed to read value.", error.toException())
                            }
                        })


                        // Sign in success, update UI with the signed-in user's information
                        Log.d("SignInActivity", "signInWithCredential:success")
                        val intent = Intent(this, HomeActivity::class.java)
                        Toast.makeText(applicationContext, "Welcome MES User!", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finish()
                    } else if(currentUser!!.email!!.contains("rishabhmehta00@gmail.com")) {
                        // Admin Login


                        // Sign in success, add user's data to firebase
                        myRef.child(currentUser.uid).addListenerForSingleValueEvent(object:
                            ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                if(snapshot.value == null) {
                                    val userModel = UserModel(uname = currentUser.displayName!!, email = currentUser.email!!, uid = currentUser.uid, type = "admin", picurl = currentUser.photoUrl.toString())
                                    myRef.child(currentUser.uid).setValue(userModel)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.w("testModel", "Failed to read value.", error.toException())
                            }

                        })

                        Log.d("SignInActivity", "signInWithCredential:success")
                        val intent = Intent(this, AdminLoginActivity::class.java)
                        Toast.makeText(applicationContext, "Welcome Admin!", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finish()

                    } else {
                        googleSignInClient.signOut()
                        Toast.makeText(applicationContext, "Error! Use MES Email to Login!", Toast.LENGTH_SHORT).show()
                    }


                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("SignInActivity", "signInWithCredential:failure")
                }
            }
    }

}