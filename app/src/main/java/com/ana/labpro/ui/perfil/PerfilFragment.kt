package com.ana.labpro.ui.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ana.labpro.R
import com.ana.labpro.databinding.FragmentPerfilBinding

class PerfilFragment : Fragment() {

    private lateinit var perfilViewModel: PerfilViewModel
    private lateinit var binding: FragmentPerfilBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        perfilViewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)

        binding.editButton.setOnClickListener {
            abrirFragmentoEdicion()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        perfilViewModel.errorMsg.observe(viewLifecycleOwner) { msg ->
            showErrorMsg(msg)
        }

        perfilViewModel.userData.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.nameTextView.text = "Nombre: ${user.namer}"
                binding.lastNameTextView.text = "Apellido: ${user.lastnamer}"
                binding.identiTextView.text = "Cédula: ${user.identir}"
                binding.programaTextView.text = "Programa: ${user.programar}"
                binding.emailTextView.text = "Email: ${user.email}"
                binding.numreservasTextView.text = "Numero de Reservas: ${user.numReservas.toString()}" 
            }
        }

        perfilViewModel.loadCurrentUser()
    }

    private fun showErrorMsg(msg: String?) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }

    private fun abrirFragmentoEdicion() {
        findNavController().navigate(R.id.action_perfilFragment_to_editPerfilFragment)
    }
}
