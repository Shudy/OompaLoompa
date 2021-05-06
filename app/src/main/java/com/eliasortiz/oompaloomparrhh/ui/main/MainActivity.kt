package com.eliasortiz.oompaloomparrhh.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eliasortiz.oompaloomparrhh.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}