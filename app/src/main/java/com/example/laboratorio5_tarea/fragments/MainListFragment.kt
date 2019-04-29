package com.example.laboratorio5_tarea.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.laboratorio5_tarea.AppConstants
import com.example.laboratorio5_tarea.MyPokemonAdapter
import com.example.laboratorio5_tarea.R
import com.example.laboratorio5_tarea.adapters.PokemonAdapter
import com.example.laboratorio5_tarea.adapters.PokemonSimpleListAdapter
import com.example.laboratorio5_tarea.models.Pokemon
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainListFragment: Fragment(){

    private lateinit var  pokemons :ArrayList<Pokemon>
    private lateinit var pokemonsAdapter : MyPokemonAdapter
    var listenerTool :  SearchNewPokemonListener? = null

    companion object {
        fun newInstance(dataset : ArrayList<Pokemon>): MainListFragment{
            val newFragment = MainListFragment()
            newFragment.pokemons = dataset
            return newFragment
        }
    }

    interface SearchNewPokemonListener{
        fun searchPokemon(pokemonName: String)

        fun managePortraitItemClick(pokemon: Pokemon)

        fun manageLandscapeItemClick(pokemon: Pokemon)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.activity_main, container, false)

        if(savedInstanceState != null) pokemons = savedInstanceState.getParcelableArrayList<Pokemon>(AppConstants.MAIN_LIST_KEY)!!

        initRecyclerView(resources.configuration.orientation, view)
        initSearchButton(view)

        return view
    }

    fun initRecyclerView(orientation:Int, container:View){
        val linearLayoutManager = LinearLayoutManager(this.context)

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            pokemonsAdapter = PokemonAdapter(pokemons, { pokemon:Pokemon->listenerTool?.managePortraitItemClick(pokemon)})
            container.rv_pokemon_list.adapter = pokemonsAdapter as PokemonAdapter
        }
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            pokemonsAdapter = PokemonSimpleListAdapter(pokemons, {pokemon:Pokemon->listenerTool?.manageLandscapeItemClick(pokemon)})
            container.rv_pokemon_list.adapter = pokemonsAdapter as PokemonSimpleListAdapter
        }

        container.rv_pokemon_list.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }
    }

    fun initSearchButton(container:View) = container.searchbarbutton.setOnClickListener {
        listenerTool?.searchPokemon(searchbar.text.toString())
    }

    fun updatePokemonAdapter(pokemonList: ArrayList<Pokemon>){ pokemonsAdapter.changeDataSet(pokemonList) }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SearchNewPokemonListener) {
            listenerTool = context
        } else {
            throw RuntimeException("Se necesita una implementación de  la interfaz")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.MAIN_LIST_KEY, pokemons)
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        listenerTool = null
    }
}