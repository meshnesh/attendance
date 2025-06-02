package com.mw.churchattendance.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mw.churchattendance.R
import com.mw.churchattendance.data.model.ScanningTarget
import com.mw.churchattendance.databinding.FragmentAddChildBinding
import com.mw.churchattendance.util.NfcTagScannedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment: Fragment(), NfcTagScannedListener {

    lateinit var binding: FragmentAddChildBinding
    private var scanningFor: ScanningTarget? = null
    private var childWristbandId: String? = null
    private var parentFobId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_child, container, false)

        return binding.root
    }

    override fun onNfcTagScanned(tagId: String) {
        when (scanningFor) {
            ScanningTarget.WRISTBAND -> {
                childWristbandId = tagId
                binding.tvWristbandTag.text = "Child Wristband: $tagId"
            }
            ScanningTarget.FOB -> {
                parentFobId = tagId
                binding.tvFobTag.text = "Parent Fob: $tagId"
            }
            null -> {
                Toast.makeText(requireContext(), "Please select wristband or fob first", Toast.LENGTH_SHORT).show()
            }
        }
    }
}