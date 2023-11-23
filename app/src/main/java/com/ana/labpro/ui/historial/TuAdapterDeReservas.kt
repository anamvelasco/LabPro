package com.ana.labpro.ui.historial

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ana.labpro.model.Reservas
import com.ana.labpro.R
import com.ana.labpro.databinding.CardViewReservaItemBinding

class TuAdapterDeReservas (
    private val reservasList : MutableList<Reservas?>,
    private val onItemClicked : (Reservas?) -> Unit,
    private val onItemLongClicked: (Reservas?) -> Unit,
): RecyclerView.Adapter <TuAdapterDeReservas.HistorialViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_reserva_item, parent, false)
        return HistorialViewHolder(view)
    }

    override fun getItemCount(): Int = reservasList.size
    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        val reserva = reservasList[position]
        holder.bind(reserva)
        holder.itemView.setOnClickListener { onItemClicked(reserva)}
        holder.itemView.setOnLongClickListener{
            onItemLongClicked(reserva)
            true
        }
    }

    fun appendItems(newList: MutableList<Reservas?>){
        reservasList.clear()
        reservasList.addAll(newList)
        notifyDataSetChanged()
    }

    class HistorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = CardViewReservaItemBinding.bind(itemView)

        fun bind(reserva: Reservas?){
            with(binding){
                nameTextView.text = reserva?.name
                emailTextView.text = reserva?.email
                programaTextView.text = reserva?.programa
                maquinaTextView.text = reserva?.maquina
                fechatextView.text= reserva?.date
                horatextView.text= reserva?.hour
            }
        }
    }
}

/*class TuAdapterDeReservas(private val reservasList: MutableList<Reservas>) :
    RecyclerView.Adapter<TuAdapterDeReservas.ReservasViewHolder>() {

    inner class ReservasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val infoReservaTextView: TextView = itemView.findViewById(R.id.infoReservaTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservasViewHolder {

        Log.d("TuAdapterDeReservas", "Paso 1: Creando ViewHolder")
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_historial, parent, false)
        return ReservasViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReservasViewHolder, position: Int) {
        Log.d("TuAdapterDeReservas", "Paso 2: Vinculando datos en posición $position")
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
        Log.d("TuAdapterDeReservas", "Paso 3: Actualizando lista con nuevos datos")
        reservasList.clear()
        reservasList.addAll(newList)
        notifyDataSetChanged()
    }
}*/
