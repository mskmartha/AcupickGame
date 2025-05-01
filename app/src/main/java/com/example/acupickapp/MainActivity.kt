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
import com.example.acupickapp.adapter.QuoteAdapter
import com.example.acupickapp.apis.NetworkMonitor
import com.example.acupickapp.apis.QuotesApi
import com.example.acupickapp.apis.RetrofitHelper
import com.example.acupickapp.apis.isInternetAvailable
import com.example.acupickapp.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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



        networkMonitor = NetworkMonitor(this)

        networkMonitor.observe(this) { isConnected ->
            if (isConnected) {
                binding.tvInternetStatus.text = "Online"
                val quotesApi = RetrofitHelper.getInstance().create(QuotesApi::class.java)

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
            } else {
                binding.tvInternetStatus.text = "Offline"
            }
        }


    }
}