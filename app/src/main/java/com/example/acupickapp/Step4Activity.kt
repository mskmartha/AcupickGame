package com.example.acupickapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.acupickapp.apis.NetworkMonitor
import com.example.acupickapp.databinding.ActivityStep4Binding
import com.handofftracker.HandOffManager

class Step4Activity : AppCompatActivity() {
    private  lateinit var binding: ActivityStep4Binding
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityStep4Binding.inflate(layoutInflater)
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
            } else {
                binding.tvInternetStatus.text = "Offline"
            }
        }


        binding.btnCompleteHandOff.setOnClickListener {

           HandOffManager.endHandOff(this)

        }
    }
}