package com.finite.digi_libraryphcet.home

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.finite.digi_libraryphcet.R
import com.finite.digi_libraryphcet.databinding.FragmentScannerBinding
import java.util.*

private const val CAMERA_REQUEST_CODE = 101

class ScannerFragment : Fragment() {

    private var binding : FragmentScannerBinding? = null
    private lateinit var codeScanner: CodeScanner
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScannerBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("testlog", "onViewCreated: ReachedScanner")
        // Scanner
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()

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
            activity.runOnUiThread {
                viewModel.scancode = it.text
                //Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                //viewModel.addItemFromDB(it.text)
            }

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    requireActivity().runOnUiThread(Runnable {
                        findNavController().navigate(ScannerFragmentDirections.actionScannerFragmentToHomeFragment())
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
        val permission = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA)

        if(permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireActivity(), "You need to give camera permission!", Toast.LENGTH_SHORT).show()
                } else {
                    // working
                }
            }
        }
    }
}