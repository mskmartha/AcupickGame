package com.handofftracker

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.TextView

object HandOffManager {
    private var startTime: Long = 0L
    private var endTime: Long = 0L
    private var points: Int = 0
    private const val PREF_NAME = "HandOffPrefs"
    private const val KEY_TOTAL_POINTS = "total_points"

    fun startHandOff() {
        startTime = System.currentTimeMillis()
    }

    fun endHandOff(context: Context) {
        endTime = System.currentTimeMillis()
        val elapsedMillis = endTime - startTime
        val elapsedMinutes = elapsedMillis / (1000 * 60)

        points = when {
            elapsedMinutes <= 2 -> 5
            elapsedMinutes in 3..5 -> 3
            else -> 1
        }

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val totalPoints = prefs.getInt(KEY_TOTAL_POINTS, 0) + points
        prefs.edit().putInt(KEY_TOTAL_POINTS, totalPoints).apply()

        showPointsDialog(context, totalPoints)
    }

    private fun showPointsDialog(context: Context, points: Int) {
        AlertDialog.Builder(context)
            .setTitle("Points Earned")
            .setMessage("You earned Total $points point(s)!")
            .setCancelable(false)
            .setPositiveButton("OK", null)
            .show()
    }


    fun getTotalPoints(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_TOTAL_POINTS, 0)
    }

    fun resetPoints(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_TOTAL_POINTS, 0).apply()
    }
}