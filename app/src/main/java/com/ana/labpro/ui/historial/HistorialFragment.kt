package com.ana.labpro.ui.historial

import HistorialViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.ana.labpro.R
import com.ana.labpro.model.Reservas
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HistorialFragment : Fragment() {

    private val historialViewModel: HistorialViewModel by lazy {
        HistorialViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)

        val reservasRecyclerView = view.findViewById<RecyclerView>(R.id.reservasRecyclerView)
        val reservasAdapter = TuAdapterDeReservas(ArrayList())
        reservasRecyclerView.adapter = reservasAdapter

        historialViewModel.reserva.observe(viewLifecycleOwner, Observer { reservas ->
            reservas?.let {
                reservasAdapter.submitList(createArrayList(it))
            }
        })

        val userEmail = obtenerEmailUsuario()
        userEmail?.let {
            GlobalScope.launch(Dispatchers.Main) {
                historialViewModel.loadReservas(it)
            }
        }

        return view
    }

    private fun obtenerEmailUsuario(): String? {
        val auth = FirebaseAuth.getInstance()
        val usuarioActual = auth.currentUser

        return try {
            usuarioActual?.email
        } catch (e: Exception) {
            Log.e("ObtenerEmailUsuario", "Error al obtener el correo electrónico del usuario: ${e.message}")
            null
        }
    }

    private fun createArrayList(reservas: Reservas): ArrayList<Reservas> {
        val arrayList = ArrayList<Reservas>()
        arrayList.add(reservas)
        return arrayList
    }

}
