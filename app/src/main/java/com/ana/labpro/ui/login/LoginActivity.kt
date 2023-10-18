package com.ana.labpro.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ana.labpro.databinding.ActivityLoginBinding
import com.ana.labpro.ui.RegisterActivity
import com.ana.labpro.ui.navdrawer.NavigationDrawerActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = activityLoginBinding.root
        setContentView(view)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val banLoginObserver = Observer<Boolean>{banLogin ->
            if (banLogin){
                val intent = Intent(this, NavigationDrawerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        loginViewModel.banLogin.observe(this,banLoginObserver)


        val errorMsgObserver = Observer<String>{errorMsg ->
            Snackbar.make(view,errorMsg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Continuar"){}
                .show()
        }

        loginViewModel.errorMsg.observe(this,errorMsgObserver)


        activityLoginBinding.loginButton.setOnClickListener {
            loginViewModel.validateData(activityLoginBinding.emailLoginEditText.text.toString(),
                activityLoginBinding.passwordLoginEditText.text.toString())
        }
        activityLoginBinding.regisButton.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }


    }
}