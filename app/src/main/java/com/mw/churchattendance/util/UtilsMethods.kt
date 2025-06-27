package com.mw.churchattendance.util

import java.util.Calendar

object UtilsMethods {
    fun getClassNameFromDob(dobMillis: Long): String {
        val today = Calendar.getInstance()
        val dob = Calendar.getInstance().apply { timeInMillis = dobMillis }

        val age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            // Birthday hasn't occurred yet this year
            return when (age - 1) {
                in 0..2 -> "crèche"
                in 3..4 -> "Little Angels"
                in 5..6 -> "Saints A"
                in 7..8 -> "Saints B"
                in 9..12 -> "Disciples"
                else -> "Teen"
            }
        }

        return when (age) {
            in 0..2 -> "crèche"
            in 3..4 -> "Little Angels"
            in 5..6 -> "Saints A"
            in 7..8 -> "Saints B"
            in 9..12 -> "Disciples"
            else -> "Teen"
        }
    }
}