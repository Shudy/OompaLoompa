package com.eliasortiz.oompaloomparrhh.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eliasortiz.oompaloomparrhh.R
import com.eliasortiz.oompaloomparrhh.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_OompaLoompaRRHH)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}