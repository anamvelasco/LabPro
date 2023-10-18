package com.ana.labpro.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ana.labpro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var fragmentBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = fragmentBinding.root
        setContentView(view)

    }
}