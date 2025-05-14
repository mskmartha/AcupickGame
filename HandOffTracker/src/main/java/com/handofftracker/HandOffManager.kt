package com.handofftracker

import android.R
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.handofftracker.apis.Api
import com.handofftracker.apis.NetworkMonitor
import com.handofftracker.apis.RetrofitHelper
import com.handofftracker.databinding.RewardScreenBinding
import com.handofftracker.extra.SharedPreferenceClass
import com.handofftracker.models.ApiResponse
import com.handofftracker.models.Rule
import kotlinx.coroutines.launch

object HandOffManager {
    private var startTime: Long = 0L
    private var endTime: Long = 0L
    private var points: Int = 0
    private const val PREF_NAME = "HandOffPrefs"
    private const val KEY_TOTAL_POINTS = "total_points"
    private lateinit var networkMonitor: NetworkMonitor
    private var codeVerified = false

    fun startHandOff() {
        startTime = System.currentTimeMillis()
    }

    fun checkCodeVerified(isVerified: Boolean) {
        codeVerified = isVerified
    }

    fun endHandOff(context: Context) {
        endTime = System.currentTimeMillis()
        val elapsedMillis = endTime - startTime
        val elapsedMinutes = elapsedMillis / (1000 * 60)
        val elapsedSeconds = elapsedMillis / 1000

        val totalTime = " ${elapsedMinutes} m ${elapsedSeconds % 60} s"

        Log.i(
            "TAG>>>",
            "endHandOff: total timing>> ${elapsedMinutes} min ${elapsedSeconds % 60} sec"
        );


        /*    points = when {
                elapsedMinutes <= 2 -> 5
                elapsedMinutes in 3..5 -> 3
                else -> 1
            }*/

        points = evaluateScoreFromRules(context, elapsedMinutes.toInt())

        if (codeVerified) {
            points = points + 1
        }


        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val totalPoints = prefs.getInt(KEY_TOTAL_POINTS, 0) + points
        prefs.edit().putInt(KEY_TOTAL_POINTS, totalPoints).apply()



        showPointsDialog(context, totalPoints, totalTime)
    }

    fun evaluateScoreFromRules(context: Context, isCompleteOrder: Int): Int {
        val data = SharedPreferenceClass(context).getValue_string("apiResponse")
        val calssData = Gson().fromJson<ApiResponse>(data, ApiResponse::class.java)

        val rules: List<Rule> = calssData.rules




        for (rule in rules) {
            val expression = rule.expression.replace(" ", "")


            // Parse simple range expressions for "isCompleteOrder"
            if (expression.contains("isCompleteOrder<=") && expression.contains("isCompleteOrder>")) {
                val lower =
                    expression.substringAfter("isCompleteOrder>").substringBefore("&&").toInt()
                val upper = expression.substringAfter("isCompleteOrder<=").toInt()
                if (isCompleteOrder > lower && isCompleteOrder <= upper) {
                    return rule.score
                }
            } else if (expression.contains("isCompleteOrder<=")) {
                val value = expression.substringAfter("isCompleteOrder<=").toInt()
                if (isCompleteOrder <= value) {
                    return rule.score
                }
            } else if (expression.contains("isCompleteOrder>")) {
                val value = expression.substringAfter("isCompleteOrder>").toInt()
                if (isCompleteOrder > value) {
                    return rule.score
                }
            }


        }

        return 0 // Default if no match
    }


    private fun showPointsDialog(context: Context, points: Int, totalTime: String) {

        val dialogBinding = RewardScreenBinding.inflate(LayoutInflater.from(context))


        dialogBinding.txtPoint.text = "$points"
        dialogBinding.txtwaitTimw.text = "$totalTime"

        val dialog = AlertDialog.Builder(context)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialogBinding.imgClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun getTotalPoints(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_TOTAL_POINTS, 0)
    }

    fun resetPoints(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_TOTAL_POINTS, 0).apply()
    }

    fun getStoreUserdata(activity: AppCompatActivity, storeNumber: String, eventUserId: String) {
        networkMonitor = NetworkMonitor(activity)

        networkMonitor.observe(activity) { isConnected ->
            if (isConnected) {
                val userApi =
                    RetrofitHelper.getInstance().create(Api::class.java)

                activity.lifecycleScope.launch {
                    try {
                        val response = userApi.getData(storeNumber, eventUserId)
                        if (response.isSuccessful && response.body() != null) {
                            val data = response.body()!!

                            val gson = Gson()
                            val parsedData = gson.toJson(data, ApiResponse::class.java)
                            Log.i("HandOffManager", "API Success: ${parsedData}")

                            SharedPreferenceClass(activity).setValue_string(
                                "apiResponse",
                                parsedData
                            )
                        } else {
                            Log.e(
                                "HandOffManager",
                                "API Error: ${response.code()} - ${response.message()}"
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("HandOffManager", "API Exception: ${e.message}")
                    }
                }
            } else {
                Log.w("HandOffManager", "No Internet Connection")
            }
        }
    }
}