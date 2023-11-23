package com.ana.labpro.ui.historial

import HistorialViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ana.labpro.R
import com.ana.labpro.databinding.FragmentHistorialBinding
import com.ana.labpro.model.Reservas
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistorialFragment : Fragment() {


    private lateinit var historialBinding: FragmentHistorialBinding
    private lateinit var historialViewModel: HistorialViewModel
    private lateinit var tuAdapterDeReservas: TuAdapterDeReservas
    private var reservasList = mutableListOf<Reservas?>()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle?): View? {

        historialViewModel = ViewModelProvider(this).get(HistorialViewModel::class.java)
        historialBinding = FragmentHistorialBinding.inflate(inflater,container, false)

        val view = historialBinding.root

        historialViewModel.loadReservas()

        historialViewModel.errorMsg.observe(viewLifecycleOwner) {msg ->
            showErrorMsg(msg)
        }

        historialViewModel.reservasList.observe(viewLifecycleOwner){reservasList ->
            tuAdapterDeReservas.appendItems(reservasList)
        }

        historialViewModel.reservaErased.observe(viewLifecycleOwner){
            //reservasList.clear()
            historialViewModel.loadReservas()
        }

        tuAdapterDeReservas = TuAdapterDeReservas(reservasList, onItemClicked = {onReservaItemClicked(it)}, onItemLongClicked = {
            onReservaLongItemClicked(it)
        })


        historialBinding.reservasRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HistorialFragment.requireContext())
            adapter = tuAdapterDeReservas
            setHasFixedSize(false)
        }

        return view

    }

    private fun onReservaLongItemClicked(reserva: Reservas?) {
        historialViewModel.deleteReserva(reserva)

    }

    private fun showErrorMsg(msg: String?) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }

    private fun onReservaItemClicked(Reserva: Reservas?) {

    }
}
    /*private val historialViewModel: HistorialViewModel by lazy {
        HistorialViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("HistorialFragment", "Paso 1: Creando vista del fragmento")
        val view = inflater.inflate(R.layout.fragment_historial, container, false)

        val reservasRecyclerView = view.findViewById<RecyclerView>(R.id.reservasRecyclerView)
        val reservasAdapter = TuAdapterDeReservas(ArrayList())
        reservasRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        reservasRecyclerView.adapter = reservasAdapter

        historialViewModel.reserva.observe(viewLifecycleOwner, Observer { reserva ->
            reserva?.let {
                Log.d("HistorialFragment", "Paso 2: Reserva obtenida: $it")
                reservasAdapter.submitList(listOf(it))
            }
        })

        val userEmail = obtenerEmailUsuario()
        userEmail?.let {
            Log.d("HistorialFragment", "Paso 3: Cargando reservas para usuario: $it")
            lifecycleScope.launch(Dispatchers.Main) {
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
    }*/

