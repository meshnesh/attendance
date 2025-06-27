package com.mw.churchattendance.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mw.churchattendance.R
import com.mw.churchattendance.data.local.entity.ChildWithParent
import com.mw.churchattendance.databinding.FragmentHomeBinding
import com.mw.churchattendance.ui.taglist.TagViewModel
import com.mw.churchattendance.util.NfcTagScannedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment(), NfcTagScannedListener {

    lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val tagViewModel: TagViewModel by viewModels()
    private var currentChild: ChildWithParent? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.viewModel = viewModel
    binding.lifecycleOwner = viewLifecycleOwner


    // Observe scanned child and update UI
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.scannedChild.collect { scanned ->
                if (scanned != null) {
                    val child = scanned.child
                    val parent = scanned.parent

                    binding.tvChildName.text = "Name: ${child.name}"
                    binding.tvChildClass.text = "Class: ${child.childClass}"
                    binding.tvParentName.text = "Parent: ${parent.name}"
                    binding.tvCheckinStatus.text =
                        if (child.isCheckedIn) "Checked In" else "Checked Out"
                    binding.tvLastCheckedIn.text =
                        viewModel.lastCheckInText.value

                    binding.btnAction.text =
                        if (child.isCheckedIn) "Check Out" else "Check In"
                    binding.btnAction.setOnClickListener {
                        viewModel.checkInChild(child)
                        Toast.makeText(
                            requireContext(),
                            "Checked in ${child.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    // Reset UI if nothing scanned
                    binding.tvChildName.text = ""
                    binding.tvChildClass.text = ""
                    binding.tvParentName.text = ""
                    binding.tvCheckinStatus.text = ""
                    binding.tvLastCheckedIn.text = ""
                    binding.btnAction.text = "Scan to Check In"
                    binding.btnAction.setOnClickListener(null)
                }
            }
        }
    }

    binding.btnAttendance.setOnClickListener{
        findNavController().navigate(R.id.action_homeFragment_to_attendanceListFragment)
    }
}

    override fun onNfcTagScanned(tagId: String) {
        // First try: is it a parent fob for check-out?
        viewModel.checkOutByParentFob(tagId) { checkOutResult ->
            if (checkOutResult != null) {
                val child = checkOutResult.child
                val parent = checkOutResult.parent

                requireActivity().runOnUiThread {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Confirm Checkout")
                        .setMessage(
                            """
                        Name: ${child.name}
                        D.O.B: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(child.dob))}
                        Parent: ${parent.name}
                        Class: ${child.childClass}
                        Registered on: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(child.dob))}
                        """.trimIndent()
                        )
                        .setPositiveButton("Checkout") { _, _ ->
                            viewModel.performCheckout(child)
                            Toast.makeText(requireContext(), "${child.name} checked out", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            } else {
                // Second try: is it a child's wristband for check-in?
                viewModel.fetchChildWithParentByWristbandId(tagId) { checkInResult ->
                    if (checkInResult != null) {
                        val child = checkInResult.child
                        val parent = checkInResult.parent

                        // Update UI with scanned child info
                        binding.tvChildName.text = "Name: ${child.name}"
                        binding.tvChildClass.text = "Class: ${child.childClass}"
                        binding.tvParentName.text = "Parent: ${parent.name}"

                        viewModel.setScannedChild(checkInResult) // Enables check-in button
                    } else {
                        // Not found in DB — go to profile creation
                        findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
                    }
                }
            }
        }
    }
}