package com.infigo.watchsaleapp.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.infigo.watchsaleapp.databinding.ActivityFullScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivityFullScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imgPath = intent.getStringExtra("imgPath")
        Glide.with(this).load(imgPath).into(binding.image)
        binding.close.setOnClickListener {
            finish()
        }
    }
}