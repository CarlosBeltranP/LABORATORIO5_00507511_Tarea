package com.example.laboratorio5_tarea.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.laboratorio5_tarea.MyPokemonAdapter
import com.example.laboratorio5_tarea.R
import kotlinx.android.synthetic.main.list_element_pokemon.view.*
import com.example.laboratorio5_tarea.models.Pokemon
import kotlinx.android.synthetic.main.cardview_pokemon.view.*

class PokemonAdapter(var pokemons: List<Pokemon>, val clickListener: (Pokemon) -> Unit):RecyclerView.Adapter<PokemonAdapter.ViewHolder>(),
    MyPokemonAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_element_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = pokemons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(pokemons[position], clickListener)

   override fun changeDataSet(newDataSet: List<Pokemon>) {
        this.pokemons = newDataSet
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: Pokemon, clickListener: (Pokemon) -> Unit) = with(itemView) {
            //tv_pokemon_id.text = item.id.toString()

            tv_pokemon_name.text = item.name
            tv_pokemon_type.text = item.url
            this.setOnClickListener { clickListener(item) }
        }
    }
}