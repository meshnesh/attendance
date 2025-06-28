package com.mw.churchattendance.ui.main

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.mw.churchattendance.R
import com.mw.churchattendance.base.BaseActivity
import com.mw.churchattendance.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Navigation if needed...

        // Makes content draw behind status bar (optional, for immersive look)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        // Set the status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.color_primary)

        // Make status bar icons dark or light depending on background
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false // false = white icons, true = dark
    }

    override fun onNfcTagScanned(tagId: String?) {
        super.onNfcTagScanned(tagId)
        tagId?.let {
//            handleScannedTag(it)
        }
    }
}