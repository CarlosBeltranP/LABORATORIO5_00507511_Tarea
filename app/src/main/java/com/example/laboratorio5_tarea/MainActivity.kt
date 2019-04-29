package com.example.laboratorio5_tarea

import android.content.Intent
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.example.laboratorio5_tarea.AppConstants
import com.example.laboratorio5_tarea.R
import com.example.laboratorio5_tarea.activities.PokemonViewer
import com.example.laboratorio5_tarea.adapters.PokemonAdapter
import com.example.laboratorio5_tarea.fragments.MainContentFragment
import com.example.laboratorio5_tarea.fragments.MainListFragment
import com.example.laboratorio5_tarea.utilities.NetworkUtils
import com.example.laboratorio5_tarea.models.Pokemon
import com.example.laboratorio5_tarea.models.TypeResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity(), MainListFragment.SearchNewPokemonListener {
    private lateinit var mainFragment : MainListFragment
    private lateinit var mainContentFragment: MainContentFragment
    private lateinit var viewAdapter: PokemonAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var pokemonList = ArrayList<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        pokemonList = savedInstanceState?.getParcelableArrayList(AppConstants.dataset_saveinstance_key) ?: ArrayList()

        initMainFragment()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.dataset_saveinstance_key, pokemonList)
        super.onSaveInstanceState(outState)
    }

    fun initMainFragment(){
        mainFragment = MainListFragment.newInstance(pokemonList)

        val resource = if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            R.id.main_fragment
        else {
            mainContentFragment = MainContentFragment.newInstance(Pokemon())
            changeFragment(R.id.land_main_cont_fragment, mainContentFragment)

            R.id.land_main_fragment
        }

        changeFragment(resource, mainFragment)
    }

    fun addPokemonToList(pokemon: Pokemon) {
        pokemonList.add(pokemon)
        mainFragment.updatePokemonAdapter(pokemonList)
        Log.d("Number", pokemonList.size.toString())


    }

    fun initRecycler(pokemon: MutableList<Pokemon>){
        viewManager = LinearLayoutManager(this)

            viewAdapter = PokemonAdapter(pokemon, { pokemonItem: Pokemon -> pokemonItemClicked(pokemonItem) })


        rv_pokemon_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun searchPokemon(pokemonName: String) {
        FetchPokemon().execute(pokemonName)
    }

    private fun pokemonItemClicked(item: Pokemon){
        startActivity(Intent(this, PokemonViewer::class.java).putExtra("CLAVIER", item.url))
    }

    override fun managePortraitItemClick(pokemon: Pokemon) {
        val pokemonBundle = Bundle()
        //pokemonBundle.putParcelable("CLE", pokemon)
        //startActivity(Intent(this, PokemonViewer::class.java).putExtras(pokemonBundle))
        startActivity(Intent(this, PokemonViewer::class.java).putExtra("CLAVIER", pokemon.url))
    }

    private fun changeFragment(id: Int, frag: Fragment){ supportFragmentManager.beginTransaction().replace(id, frag).commit() }

    override fun manageLandscapeItemClick(pokemon: Pokemon) {
        mainContentFragment = MainContentFragment.newInstance(pokemon)
        changeFragment(R.id.land_main_cont_fragment, mainContentFragment)
    }
    private inner class FetchPokemon : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {

            if (params.isNullOrEmpty()) return ""

            val pokemonName = params[0]
            val pokemonUrl = NetworkUtils().buildUrl("type", pokemonName)

            return try {
                NetworkUtils().getResponseFromHttpUrl(pokemonUrl)
            } catch (e: IOException) {
                ""
            }
        }

        override fun onPostExecute(pokemonInfo: String) {
            val pokemon = if (!pokemonInfo.isEmpty()) {
                val root = JSONObject(pokemonInfo)
                val results = root.getJSONArray("pokemon")
                MutableList(20) { i ->
                    val resulty = JSONObject(results[i].toString())
                    val result = JSONObject(resulty.getString("pokemon"))

                    Pokemon(i,
                        result.getString("name").capitalize(),
                        R.string.n_a_value.toString(),
                        R.string.n_a_value.toString(),
                        R.string.n_a_value.toString(),
                        R.string.n_a_value.toString(),
                        result.getString("url"),
                        R.string.n_a_value.toString())
                }
            } else {
                MutableList(20) { i ->
                    Pokemon(i, R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(),R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString(), R.string.n_a_value.toString())
                }
            }
            initRecycler(pokemon)
        }
    }

}

