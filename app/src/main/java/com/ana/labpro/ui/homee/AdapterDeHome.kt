package com.ana.labpro.ui.homee

import androidx.recyclerview.widget.RecyclerView
import com.ana.labpro.model.Galeria
import com.ana.labpro.ui.homee.AdapterDeHome
import com.squareup.picasso.Picasso

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ana.labpro.R
import com.ana.labpro.databinding.CardViewGalleryItemBinding
import com.ana.labpro.databinding.CardViewReservaItemBinding
import com.ana.labpro.model.Reservas

class AdapterDeHome(
private val galeryList: MutableList<Galeria?>,
private val onItemClicked: (Galeria?) -> Unit,
private val onItemLongClicked: (Galeria?) -> Unit,
) : RecyclerView.Adapter<AdapterDeHome.HomeeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_gallery_item, parent, false)
        return HomeeViewHolder(view)
    }

    override fun getItemCount(): Int = galeryList.size

    override fun onBindViewHolder(holder: HomeeViewHolder, position: Int) {
        val galeria = galeryList[position]
        holder.bind(galeria)
        holder.itemView.setOnClickListener { onItemClicked(galeria) }
        holder.itemView.setOnLongClickListener {
            onItemLongClicked(galeria)
            Log.d("AdapterDeHome", "Clic largo ejecutado para posición: $position")
            true
        }
    }

    fun appendItems(newList: MutableList<Galeria?>) {
        galeryList.clear()
        galeryList.addAll(newList)
        notifyDataSetChanged()
    }

    class HomeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CardViewGalleryItemBinding.bind(itemView)

        fun bind(galeria: Galeria?) {
            with(binding) {
                nameGalleryText.text = galeria?.name

                if (!galeria?.urlPicture.isNullOrEmpty()) {
                    // Cargar la imagen con Picasso solo si la URL no está vacía o nula
                    if (galeria != null) {
                        Picasso.get().load(galeria.urlPicture).into(galleryImageView)
                    }
                } else {
                    // Si urlPicture es nulo o vacío, establecer una imagen por defecto
                    galleryImageView.setImageResource(R.drawable.logo)
                }
            }
        }
/*

        fun bind(galeria: Galeria?) {
            with(binding) {
                nameGalleryText.text = galeria?.name

                if (galeria?.urlPicture != null) {
                    // Cargar la imagen con Picasso
                    Picasso.get().load(galeria.urlPicture).into(galleryImageView)
                } else {
                    // Si urlPicture es nulo, establecer una imagen por defecto
                    galleryImageView.setImageResource(R.drawable.logo)
                }
            }
        }*/
    }
}
