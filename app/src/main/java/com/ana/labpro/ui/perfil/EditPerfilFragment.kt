package com.ana.labpro.ui.perfil

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ana.labpro.R
import com.ana.labpro.databinding.FragmentEditPerfilBinding
import com.ana.labpro.model.User
import androidx.navigation.fragment.findNavController

class EditPerfilFragment : Fragment() {

    private lateinit var perfilViewModel: PerfilViewModel
    private lateinit var binding: FragmentEditPerfilBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        perfilViewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)

        observeViewModel()

        binding.saveButton.setOnClickListener {
            val editedUser = obtenerDatosEditados()
            guardarDatosEditados(editedUser)
        }
    }

    private fun observeViewModel() {
        perfilViewModel.userData.observe(viewLifecycleOwner) { user ->
            user?.let {
                actualizarInterfazUsuario(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        perfilViewModel.loadCurrentUser()
    }

    private fun obtenerDatosEditados(): User {
        val currentUser = perfilViewModel.userData.value
        val editedUser = User(
            uid = currentUser?.uid,
            email = currentUser?.email,
            namer = binding.nameEditText.text.toString(),
            lastnamer = binding.lastNameEditText.text.toString(),
            identir = binding.identiEditText.text.toString(),
            programar = obtenerProgramaSeleccionado(),
            numReservas = currentUser?.numReservas ?: 0
        )


        Log.d("EditPerfilFragment", "Datos editados: $editedUser")

        return editedUser
    }



    private fun obtenerProgramaSeleccionado(): String {
        return binding.programaSpinnerEdit.selectedItem.toString()
    }

    private fun guardarDatosEditados(user: User) {
        Log.d("EditPerfilFragment", "Guardando datos editados: $user")
        perfilViewModel.actualizarUsuario(user)
        // Navegar hacia atrás en la pila de fragmentos
        findNavController().navigateUp()
    }




    private fun actualizarInterfazUsuario(user: User) {
        binding.nameEditText.setText(user.namer)
        binding.lastNameEditText.setText(user.lastnamer)
        binding.identiEditText.setText(user.identir)
        binding.programaSpinnerEdit.setSelection(obtenerIndicePrograma(user.programar))
    }

    private fun obtenerIndicePrograma(programa: String?): Int {
        val programaOptions = resources.getStringArray(R.array.programa_options)
        return programaOptions.indexOf(programa)
    }
}
