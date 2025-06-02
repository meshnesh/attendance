package com.mw.churchattendance.util

interface NfcTagScannedListener {
    fun onNfcTagScanned(tagId: String)
}