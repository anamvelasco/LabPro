package com.ana.labpro.ui.homee

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ana.labpro.R
import com.ana.labpro.databinding.FragmentHomeeBinding
import com.ana.labpro.model.Galeria
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class HomeeFragment : Fragment() {

    private lateinit var homeeBinding: FragmentHomeeBinding
    private lateinit var homeeViewModel: HomeeViewModel
    private lateinit var adapterDeHome: AdapterDeHome
    private var galleryList = mutableListOf<Galeria?>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeeViewModel = ViewModelProvider(this).get(HomeeViewModel::class.java)
        homeeBinding = FragmentHomeeBinding.inflate(inflater, container, false)

        // Corrección: Utiliza homeeBinding.root para obtener la vista inflada
        val createGalleryButton = homeeBinding.root.findViewById<Button>(R.id.createGalleryButton)
        createGalleryButton.setOnClickListener {
            lifecycleScope.launch {
                val currentUserRole = homeeViewModel.getCurrentUserRole()
                if (currentUserRole == "admin") {
                    showCreateGalleryDialog()
                } else {
                    showErrorMsg("No tienes permisos para crear una galería.")
                }
            }
        }


        val view = homeeBinding.root

        adapterDeHome = AdapterDeHome(
            galleryList,
            onItemClicked = { onGaleriaItemClicked(it) },
            onItemLongClicked = { onGaleriaLongItemClicked(it) }
        )

        homeeBinding.galeryRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeeFragment.requireContext())
            adapter = adapterDeHome
            setHasFixedSize(false)
        }

        homeeViewModel.loadGalery()

        homeeViewModel.errorMsg.observe(viewLifecycleOwner) { msg ->
            showErrorMsg(msg)
        }
        homeeViewModel.galleryList.observe(viewLifecycleOwner) { galeriaList ->
            // Actualiza la lista de galerías en el adaptador y notifica los cambios
            adapterDeHome.appendItems(galeriaList)
        }

        return homeeBinding.root

    }


    private fun showCreateGalleryDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_create_galeria, null)

        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val editTextUrl = dialogView.findViewById<EditText>(R.id.editTextUrl)

        alertDialogBuilder.setView(dialogView)
        alertDialogBuilder.setTitle("Crear Nueva Galería")

        alertDialogBuilder.setPositiveButton("Crear") { dialog, _ ->
            val name = editTextName.text.toString()
            val urlPicture = editTextUrl.text.toString()

            lifecycleScope.launch {
                if (!homeeViewModel.createGaleria(name, urlPicture)) {
                    showErrorMsg("No tienes permisos para crear una galería.")
                }
            }

            dialog.dismiss()
        }

        alertDialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.create().show()
    }



    private fun showErrorMsg(msg: String?) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }

    private fun onGaleriaItemClicked(galeria: Galeria?) {
        // Lógica para manejar el clic en una galería
    }

    private fun onGaleriaLongItemClicked(galeria: Galeria?) {
        galeria?.let {
            lifecycleScope.launch {
                // Realizar validación del rol
                val userRole = homeeViewModel.getCurrentUserRole()

                if (userRole == "admin") {
                    // Si tiene permisos, mostrar el AlertDialog
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setTitle("Eliminar Galería")
                    alertDialogBuilder.setMessage("¿Estás seguro de que quieres eliminar esta galería?")

                    alertDialogBuilder.setPositiveButton("Sí") { _, _ ->
                        // Lógica para eliminar la galería
                        lifecycleScope.launch {
                            homeeViewModel.deleteImagenConValidacionDeRol(galeria)
                        }
                    }

                    alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }

                    alertDialogBuilder.create().show()
                } else {
                    // Si no tiene permisos, mostrar mensaje de error
                    showErrorMsg("No tienes permisos para eliminar una galería.")
                }
            }
        }
    }


}
