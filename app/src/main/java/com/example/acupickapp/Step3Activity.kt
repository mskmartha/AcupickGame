package com.example.acupickapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.acupickapp.Step2Activity
import com.example.acupickapp.apis.NetworkMonitor
import com.example.acupickapp.databinding.ActivityStep3Binding
import com.handofftracker.HandOffManager

class Step3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityStep3Binding
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStep3Binding.inflate(layoutInflater)
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




        binding.btnVerifyCode.setOnClickListener {

            HandOffManager.checkCodeVerified(true)
            startActivity(Intent(this@Step3Activity, Step4Activity::class.java))


        }

        binding.btnSkipCode.setOnClickListener {
            startActivity(Intent(this@Step3Activity, Step4Activity::class.java))

        }
    }
}