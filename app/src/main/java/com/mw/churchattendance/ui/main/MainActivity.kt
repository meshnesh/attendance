package com.mw.churchattendance.ui.main

import android.os.Bundle
import android.util.Log
import com.mw.churchattendance.databinding.ActivityMainBinding
import com.mw.churchattendance.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        tagId?.let {
            handleScannedTag(it)
        }
    }
}