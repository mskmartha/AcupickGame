package com.example.acupickapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.acupickapp.Model.Example
import com.example.acupickapp.Model.Rule
import com.example.acupickapp.Model.Score
import com.example.acupickapp.Model.UserData
import com.example.acupickapp.adapter.QuoteAdapter
import com.example.acupickapp.apis.NetworkMonitor
import com.example.acupickapp.apis.Api
import com.example.acupickapp.apis.RetrofitHelper
import com.example.acupickapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.handofftracker.HandOffManager
import com.handofftracker.models.ApiResponse
import kotlinx.coroutines.launch
import java.io.StringReader

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var networkMonitor: NetworkMonitor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        var score1 = Score(0, 2, 5)
        var score2 = Score(2, 5, 3)
        var score3 = Score(5, 6, 0)
        var scoreList: List<Score> = listOf(score1, score2, score3)
        var rule1 =
            Rule("R2", "Auth_Code_Exists", "eventReasonCode ==='CODE_VERIFIED'", listOf<Score>(), 0)
        var rule2 = Rule("R4", "Complete_Order", "isCompleteOrder ==='true'", scoreList, 0)

        var listtt: List<Rule> = listOf(rule2, rule1)
        var userData = UserData("14", "4174", "100.0", "2345", listtt, "6", "6")
        var example = Example("1883", "smar602", userData)


        networkMonitor = NetworkMonitor(this)

        networkMonitor.observe(this) { isConnected ->
            if (isConnected) {
                binding.tvInternetStatus.text = "Online"
                val quotesApi = RetrofitHelper.getInstance(RetrofitHelper.baseUrl).create(Api::class.java)

                binding.rvList.layoutManager = LinearLayoutManager(this)

                lifecycleScope.launch {
                    try {
                        val response = quotesApi.getQuotes()
                        if (response.isSuccessful && response.body() != null) {
                            val quotes = response.body()!!
                            val adapter = QuoteAdapter(quotes) { quote ->
                                val intent = Intent(this@MainActivity, Step2Activity::class.java)
                                intent.putExtra("title", quote.title)
                                intent.putExtra("body", quote.body)
                                startActivity(intent)
                            }
                            binding.rvList.adapter = adapter


                        } else {
                            Log.e("QuotesAPI", "Error: ${response.code()} - ${response.message()}")
                        }
                    } catch (e: Exception) {
                        Log.e("QuotesAPI", "Exception: ${e.message}")
                    }
                }

                HandOffManager.getStoreUserdata(this@MainActivity,"1883","smar602")

//                val userApi =
//                    RetrofitHelper.getInstance(RetrofitHelper.baseUrl1).create(
//                        Api::class.java)
//
//                lifecycleScope.launch {
//                    try {
//                        val response = userApi.getData()
//                        if (response.isSuccessful && response.body() != null) {
//                            val data: ApiResponse = response.body()!!
//
//                            val datas = Gson().fromJson<ApiResponse>(data.toString(), ApiResponse::class.java)
//
//                            Log.i("HandOffManager", "API Success: $datas")
//                        } else {
//                            Log.e(
//                                "HandOffManager",
//                                "API Error: ${response.code()} - ${response.errorBody()}"
//                            )
//                        }
//                    } catch (e: Exception) {
//                        Log.e("HandOffManager", "API Exception: ${e.message}")
//                    }
//                }


            } else {
                binding.tvInternetStatus.text = "Offline"
            }
        }
    }
}