package com.ana.labpro.ui.registro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ana.labpro.databinding.ActivityRegisterBinding
import com.ana.labpro.model.User
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

        val banRegisterObserver = Observer<Boolean> { banRegister ->
            if (banRegister) {
                val intent = Intent(this, NavigationDrawerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        registerViewModel.banRegister.observe(this, banRegisterObserver)

        val errorMsgObserver = Observer<String?> { errorMsg ->
            errorMsg?.let {
                Snackbar.make(view, it, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Continuar") {}
                    .show()
            }
        }

        registerViewModel.errorMsg.observe(this, errorMsgObserver)

        registerViewModel.registerSuccess.observe(this) {
            onBackPressedDispatcher.onBackPressed()
        }

        activityRegisterBinding.registerButton.setOnClickListener {
            val email = activityRegisterBinding.emailEditText.text.toString()
            val password = activityRegisterBinding.passwordEditText.text.toString()
            val repeatPassword = activityRegisterBinding.repPasswordEditText.text.toString()

            val name = activityRegisterBinding.nameEditText.text.toString()
            val lastName = activityRegisterBinding.lastNameEditText.text.toString()
            val cedula = activityRegisterBinding.cedulaEditText.text.toString()
            val programa = activityRegisterBinding.programaSpinner.selectedItem.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()) {
                registerViewModel.validateData(email, password, repeatPassword, name, lastName, cedula, programa)
            } else {
                Snackbar.make(view, "Por favor, completa todos los campos", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
