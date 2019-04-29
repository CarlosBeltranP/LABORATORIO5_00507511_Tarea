package com.example.laboratorio5_tarea

import com.example.laboratorio5_tarea.models.Pokemon

object AppConstants{
    val dataset_saveinstance_key = "CLE"
    val MAIN_LIST_KEY = "key_list_pokemons"
}

interface MyPokemonAdapter {
    fun changeDataSet(newDataSet : List<Pokemon>)
}