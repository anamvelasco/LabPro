package com.ana.labpro.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View  // Asegúrate de importar la clase correcta
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ana.labpro.R
import com.ana.labpro.databinding.ActivityLoginBinding
import com.ana.labpro.ui.registro.RegisterActivity
import com.ana.labpro.ui.navdrawer.NavigationDrawerActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    private val _errorMsg: MutableLiveData<String?> = MutableLiveData()
    val errorMsg: MutableLiveData<String?> = _errorMsg

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        val view = activityLoginBinding.root
        setContentView(view)

        val banLoginObserver = Observer<Boolean> { banLogin ->
            if (banLogin) {
                val intent = Intent(this, NavigationDrawerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        loginViewModel.banLogin.observe(this, banLoginObserver)

        val errorMsgObserver = Observer<String> { errorMsg ->
            Snackbar.make(view, errorMsg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Continuar") {}
                .show()
        }

        loginViewModel.errorMsg.observe(this, errorMsgObserver)

        activityLoginBinding.loginButton.setOnClickListener {
            loginViewModel.validateData(
                activityLoginBinding.emailLoginEditText.text.toString(),
                activityLoginBinding.passwordLoginEditText.text.toString()
            )
        }
        activityLoginBinding.regisButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginViewModel.verifyUser()

        val forgotTextView: android.widget.TextView = findViewById(R.id.forgot_text_view)
        forgotTextView.setOnClickListener {
            forgotPasswordClicked(it)
        }

        loginViewModel.errorMsg.observe(this) { msg ->
            showErrorMsg(msg)
        }
    }

    fun forgotPasswordClicked(view: View) {
        val email = activityLoginBinding.emailLoginEditText.text.toString()

        if (email.isNotEmpty()) {
            // Verificar si el correo electrónico existe en Firestore
            checkIfUserExists(email)
        } else {
            Toast.makeText(this, "Ingrese su dirección de correo electrónico primero.", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkIfUserExists(email: String) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")

        usersCollection.whereEqualTo("email", email).get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // El correo electrónico no está asociado con ninguna cuenta en Firestore
                    Toast.makeText(
                        this,
                        "No hay ninguna cuenta asociada con este correo electrónico.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    // El correo electrónico está asociado con una cuenta, enviar el correo de restablecimiento
                    sendPasswordResetEmail(email)
                }
            }
            .addOnFailureListener { exception ->
                // Error al consultar Firestore
                Toast.makeText(
                    this,
                    "Error al verificar la existencia del usuario. Inténtalo de nuevo.",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun sendPasswordResetEmail(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Correo de recuperación enviado. Revise su bandeja de entrada.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Error al enviar el correo de recuperación. Verifique su dirección de correo electrónico.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


    private fun showErrorMsg(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
