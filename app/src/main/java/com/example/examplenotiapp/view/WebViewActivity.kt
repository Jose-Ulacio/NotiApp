package com.example.examplenotiapp.view

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.examplenotiapp.R
import com.example.examplenotiapp.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        mBinding = ActivityWebViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        val link = intent.getStringExtra("link")
        Log.e("Link", link.toString())

        val web = findViewById<WebView>(R.id.web)
        web.settings.javaScriptEnabled = true
        link?.let { web.loadUrl(it) }

        mBinding.btnBack.setOnClickListener{
            finish()
        }


    }
}