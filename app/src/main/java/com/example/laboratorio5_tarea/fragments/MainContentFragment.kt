package com.example.laboratorio5_tarea.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.laboratorio5_tarea.R
import com.example.laboratorio5_tarea.models.Pokemon
import kotlinx.android.synthetic.main.main_content_fragment_layout.view.*
import kotlinx.android.synthetic.main.viewer_element_pokemon.*
import kotlinx.android.synthetic.main.viewer_element_pokemon.view.*

class MainContentFragment: Fragment() {

    var pokemon = Pokemon()

    companion object {
        fun newInstance(pokemon: Pokemon): MainContentFragment{
            val newFragment = MainContentFragment()
            newFragment.pokemon = pokemon
            return newFragment
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.viewer_element_pokemon, container, false)

        bindData(view)

        return view
    }

    fun bindData(view: View){
        view.weight.text = pokemon.weight

        view.fstType.text = pokemon.fsttype
        view.sndType.text = pokemon.sndtype

    }

}