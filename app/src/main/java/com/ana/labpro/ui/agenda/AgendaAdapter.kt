package com.ana.labpro.ui.agenda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ana.labpro.R
import com.ana.labpro.databinding.CardViewCalendarItemBinding
import com.ana.labpro.model.Reservas

class AgendaAdapter (
    private val reservasList : MutableList<Reservas?>,
    private val onItemClicked : (Reservas?) -> Unit,
    private val onItemLongClicked: (Reservas?) -> Unit,
): RecyclerView.Adapter <AgendaAdapter.AgendaViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_calendar_item, parent, false)
        return AgendaViewHolder(view)
    }
    override fun getItemCount(): Int = reservasList.size
    override fun onBindViewHolder(holder: AgendaAdapter.AgendaViewHolder, position: Int) {
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

    class AgendaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = CardViewCalendarItemBinding.bind(itemView)

        fun bind(reserva: Reservas?){
            with(binding){
                nameTextView.text = reserva?.name
                //emailTextView.text = reserva?.email
                //programaTextView.text = reserva?.programa
                maquinaTextView.text = reserva?.maquina
                //fechatextView.text= reserva?.date
                horatextView.text= reserva?.hour
            }
        }
    }
}
