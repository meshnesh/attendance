package com.mw.churchattendance.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mw.churchattendance.R
import com.mw.churchattendance.databinding.ActivityMainBinding
import com.mw.churchattendance.ui.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Navigation if needed...
    }

    override fun onNfcTagScanned(tagId: String?) {
        super.onNfcTagScanned(tagId)
        Log.d("MainActivity", "NFC Tag Scanned: $tagId")
        // TODO: Pass to fragment or trigger check-in/out logic
    }
}