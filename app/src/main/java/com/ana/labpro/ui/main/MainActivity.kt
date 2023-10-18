package com.ana.labpro.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ana.labpro.R
import com.ana.labpro.databinding.ActivityMainBinding
import com.ana.labpro.ui.RegisterActivity
import com.ana.labpro.ui.login.LoginActivity
import com.ana.labpro.ui.registro.RegistroFragment

class MainActivity : AppCompatActivity() {
    private lateinit var fragmentBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = fragmentBinding.root
        setContentView(view)

    }
}