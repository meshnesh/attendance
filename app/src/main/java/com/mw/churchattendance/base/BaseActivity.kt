package com.mw.churchattendance.base

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mw.churchattendance.data.local.entity.NfcTag
import com.mw.churchattendance.data.local.entity.TagType
import com.mw.churchattendance.ui.taglist.TagViewModel
import kotlinx.coroutines.launch

open class BaseActivity : AppCompatActivity() {

    private lateinit var viewModel: TagViewModel
    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize NFC
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
        val now = System.currentTimeMillis()

        viewModel.getTagById(tagId) { existingTag ->
            lifecycleScope.launch {
                if (tagId.startsWith("C")) {
                    if (existingTag == null) {
                        viewModel.insertTag(
                            NfcTag(
                                tagId = tagId,
                                childName = "Unknown Child", // optionally update this
                                tagType = TagType.WRISTBAND,
                                isCheckedIn = true,
                                lastActionTimestamp = now
                            )
                        )
                        Toast.makeText(this@BaseActivity, "Child checked in", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.updateTag(
                            existingTag.copy(
                                isCheckedIn = true,
                                lastActionTimestamp = now
                            )
                        )
                        Toast.makeText(this@BaseActivity, "Child re-checked in", Toast.LENGTH_SHORT).show()
                    }
                } else if (tagId.startsWith("P")) {
                    val childToCheckOut = viewModel.allTags.value.firstOrNull { it.isCheckedIn }
                    if (childToCheckOut != null) {
                        viewModel.updateTag(
                            childToCheckOut.copy(
                                isCheckedIn = false,
                                isCheckedOut = true,
                                lastActionTimestamp = now
                            )
                        )
                        Toast.makeText(this@BaseActivity, "Child checked out", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@BaseActivity, "No child found for checkout", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /**
     * Child classes should override this method to handle the tag.
     */
    open fun onNfcTagScanned(tagId: String?) {
        Toast.makeText(this, "Scanned Tag: $tagId", Toast.LENGTH_SHORT).show()
    }
}