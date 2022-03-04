package com.finite.digi_libraryphcet.adapter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.finite.digi_libraryphcet.DetailActivity
import com.finite.digi_libraryphcet.R
import com.finite.digi_libraryphcet.databinding.ActivityScanBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

private const val CAMERA_REQUEST_CODE = 101

class ScanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityScanBinding
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("testlog", "onViewCreated: ReachedScanner")
        // Scanner
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = applicationContext

        setupPermissions()

        codeScanner = CodeScanner(activity, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.CONTINUOUS // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
//                viewModel.scancode = it.text
                val database = Firebase.database
                val myRef = database.getReference("currentCode")

                val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("currentCode",it.text)
                editor.apply()

                myRef.setValue(it.text)

                //Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                //viewModel.addItemFromDB(it.text)
            }

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread(Runnable {
//                        findNavController().navigate(ScannerFragmentDirections.actionScannerFragmentToHomeFragment())
                        val intent = Intent(this@ScanActivity, DetailActivity::class.java)
                        startActivity(intent)
                        finish()
                    })
                }
            }, 1000)
            /*
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    findNavController().navigate(ScannerFragmentDirections.actionScannerFragmentToCartFragment())
                }
            }, 1000)*/
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if(permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You need to give camera permission!", Toast.LENGTH_SHORT).show()
                } else {
                    // working
                }
            }
        }
    }
}