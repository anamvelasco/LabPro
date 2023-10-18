package com.ana.labpro.ui.registro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ana.labpro.databinding.ActivityLoginBinding
import com.ana.labpro.databinding.ActivityRegisterBinding
import com.ana.labpro.ui.login.LoginActivity
import com.ana.labpro.ui.registro.RegisterActivity
import com.ana.labpro.ui.navdrawer.NavigationDrawerActivity
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
    private lateinit var activityRegisterBinding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = activityRegisterBinding.root
        setContentView(view)

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        val banLoginObserver = Observer<Boolean>{banLogin ->
            if (banLogin){
                val intent = Intent(this, NavigationDrawerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        registerViewModel.banLogin.observe(this,banLoginObserver)


        val errorMsgObserver = Observer<String>{errorMsg ->
            Snackbar.make(view,errorMsg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Continuar"){}
                .show()
        }

        registerViewModel.errorMsg.observe(this,errorMsgObserver)


        activityRegisterBinding.registerButton.setOnClickListener {
            registerViewModel.validateData(activityRegisterBinding.emailEditText.text.toString(),
                activityRegisterBinding.passwordEditText.text.toString())
        }
        activityRegisterBinding.registerButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }
}