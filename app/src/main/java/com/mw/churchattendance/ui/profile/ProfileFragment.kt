package com.mw.churchattendance.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mw.churchattendance.R
import com.mw.churchattendance.base.BaseFragment
import com.mw.churchattendance.data.local.entity.child.Child
import com.mw.churchattendance.data.local.entity.parent.Parent
import com.mw.churchattendance.data.model.ScanningTarget
import com.mw.churchattendance.databinding.FragmentAddChildBinding
import com.mw.churchattendance.ui.taglist.TagViewModel
import com.mw.churchattendance.util.NfcTagScannedListener
import com.mw.churchattendance.util.UtilsMethods
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.getValue

@AndroidEntryPoint
class ProfileFragment: BaseFragment(), NfcTagScannedListener {

    lateinit var binding: FragmentAddChildBinding
    private var scanningFor: ScanningTarget? = null
    private var childWristbandId: String? = null
    private var parentFobId: String? = null
    private var selectedDob: Long? = null

    private val viewModel: TagViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_child, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDob.setOnClickListener {
            showDatePicker()
        }

        binding.tvWristbandTag.setOnClickListener {
            scanningFor = ScanningTarget.WRISTBAND
            Toast.makeText(requireContext(), "Ready to scan wristband", Toast.LENGTH_SHORT).show()
        }

        binding.tvFobTag.setOnClickListener {
            scanningFor = ScanningTarget.FOB
            Toast.makeText(requireContext(), "Ready to scan parent fob", Toast.LENGTH_SHORT).show()
        }

        binding.btnSave.setOnClickListener {
            val childName = binding.etChildName.text.toString()
            val dob = selectedDob ?: return@setOnClickListener
            val childClass = binding.etChildClass.text.toString()
            val parentName = binding.etParentName.text.toString()
            val parentPhone = binding.etParentPhone.text.toString()

            if (childName.isBlank() || childClass.isBlank() || parentName.isBlank() || parentPhone.isBlank() || childWristbandId == null || parentFobId == null
            ) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val child = Child(
                wristbandId = childWristbandId!!,
                name = childName,
                dob = dob.toLong(),
                childClass = childClass,
                parentFobId = parentFobId!!
            )

            val parent = Parent(
                fobId = parentFobId!!,
                name = parentName,
                phone = parentPhone
            )

            viewModel.saveProfile(child, parent)

            showSuccessDialog(
                SpannableString("${child.name} Saved Successfully"),
                getString(R.string.dialog_success), true,
                callback = {
                    dialog?.dismiss()
                    findNavController().navigateUp()
                }
            )
            Log.d("TAG_DEBUG", "Collected Child details: $child and this is the parents details $parent")
        }
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

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDob = calendar.timeInMillis

                val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    .format(calendar.time)
                binding.tvDob.text = formattedDate

                // Auto-fill child class
                val childClass = UtilsMethods.getClassNameFromDob(selectedDob!!)
                binding.etChildClass.setText(childClass)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.datePicker.maxDate = System.currentTimeMillis()
        datePicker.show()
    }
}