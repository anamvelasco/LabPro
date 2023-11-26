package com.ana.labpro.ui.agenda

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ana.labpro.R
import com.ana.labpro.databinding.FragmentAgendaBinding
import com.ana.labpro.model.Reservas
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.SimpleDateFormat
import java.util.*

class AgendaFragment : Fragment() {

    private lateinit var agendaBinding: FragmentAgendaBinding
    private lateinit var agendaViewModel: AgendaViewModel
    private lateinit var agendaAdapter: AgendaAdapter
    private var fecha: String = ""
    private var reservasList = mutableListOf<Reservas?>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        agendaViewModel = ViewModelProvider(this).get(AgendaViewModel::class.java)
        agendaBinding = FragmentAgendaBinding.inflate(layoutInflater)

        val view = agendaBinding.root

        agendaViewModel.errorMsg.observe(viewLifecycleOwner) {msg ->
            showErrorMsg(msg)
        }

        agendaViewModel.reservasList.observe(viewLifecycleOwner){reservasList ->
            agendaAdapter.appendItems(reservasList)
        }

        //SUPERUSUARIO
        agendaViewModel.reservaErased.observe(viewLifecycleOwner){
            //reservasList.clear()
            agendaViewModel.loadReservasbyDate(fecha)
        }

        agendaAdapter = AgendaAdapter(reservasList, onItemClicked = {onReservaItemClicked(it)}, onItemLongClicked = {
            onReservaLongItemClicked(it)
        })

        agendaBinding.calendarRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AgendaFragment.requireContext())
            adapter = agendaAdapter
            setHasFixedSize(false)
        }


        val calendarView: MaterialCalendarView = view.findViewById(R.id.calendarView)
        calendarView.setOnDateChangedListener { widget, date, selected ->

            fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.date)
            Log.d("Fecha", fecha)
            Log.d("Fecha Selected", selected.toString())

            agendaViewModel.loadReservasbyDate(fecha)


        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)








    }

    private fun showErrorMsg(msg: String?) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }

    private fun onReservaItemClicked(Reserva: Reservas?) {

    }

    private fun onReservaLongItemClicked(reserva: Reservas?) {
        agendaViewModel.deleteReservaConValidacionDeRol(reserva)

    }


}