package com.ana.labpro.ui.historial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ana.labpro.model.Reservas
import com.ana.labpro.R

class TuAdapterDeReservas(private val reservasList: MutableList<Reservas>) :
    RecyclerView.Adapter<TuAdapterDeReservas.ReservasViewHolder>() {

    inner class ReservasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val infoReservaTextView: TextView = itemView.findViewById(R.id.infoReservaTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservasViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reserva, parent, false)
        return ReservasViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReservasViewHolder, position: Int) {
        val currentReserva = reservasList[position]

        val infoReservaText = """
            Nombre: ${currentReserva.name}
            Cédula: ${currentReserva.cedula}
            Email: ${currentReserva.email}
            Programa: ${currentReserva.programa}
            Máquina: ${currentReserva.maquina}
            Fecha: ${currentReserva.date}
            Hora: ${currentReserva.hour}
        """.trimIndent()

        holder.infoReservaTextView.text = infoReservaText
    }

    override fun getItemCount() = reservasList.size

    fun submitList(newList: List<Reservas>) {
        reservasList.clear()
        reservasList.addAll(newList)
        notifyDataSetChanged()
    }
}
