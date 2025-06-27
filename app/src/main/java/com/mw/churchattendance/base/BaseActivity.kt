package com.mw.churchattendance.base

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mw.churchattendance.R
import com.mw.churchattendance.data.local.entity.tags.NfcTag
import com.mw.churchattendance.data.local.entity.tags.TagType
import com.mw.churchattendance.ui.taglist.TagViewModel
import com.mw.churchattendance.util.NfcTagScannedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    private val viewModel: TagViewModel by viewModels()

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC not supported on this device", Toast.LENGTH_LONG).show()
            finish()
        } else {
            pendingIntent = PendingIntent.getActivity(
                this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            val tagId = tag?.id?.joinToString("") { String.format("%02X", it) }

            onNfcTagScanned(tagId)
        }
    }

    fun handleScannedTag(tagId: String) {
        Log.d("TAG_DEBUG", "handlingScannedTag tags: $tagId")
        val now = System.currentTimeMillis()

        viewModel.getTagById(tagId) { existingTag ->
            lifecycleScope.launch {
                if (existingTag == null) {
                    viewModel.insertTag(
                        NfcTag(
                            tagId = tagId,
                            childName = "Unknown Child",
                            tagType = TagType.FOB,
                            isCheckedIn = true,
                            lastActionTimestamp = now
                        )
                    )
                    Toast.makeText(this@BaseActivity, "Tag added & checked in", Toast.LENGTH_SHORT).show()
                } else {
                    val updated = existingTag.copy(
                        isCheckedIn = !existingTag.isCheckedIn,
                        isCheckedOut = existingTag.isCheckedIn,
                        lastActionTimestamp = now
                    )
                    viewModel.updateTag(updated)

                    val action = if (updated.isCheckedIn) "checked in" else "checked out"
                    Toast.makeText(this@BaseActivity, "Tag $action", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    open fun onNfcTagScanned(tagId: String?) {
        if (tagId == null) return

        // Try to pass the tag ID to the visible fragment
        val currentFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?.childFragmentManager
            ?.fragments
            ?.firstOrNull()

        if (currentFragment is NfcTagScannedListener) {
            currentFragment.onNfcTagScanned(tagId)
        } else {
            Toast.makeText(this, "Scanned Tag: $tagId", Toast.LENGTH_SHORT).show()
        }
    }
}
