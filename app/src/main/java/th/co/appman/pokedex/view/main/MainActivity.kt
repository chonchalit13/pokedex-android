package th.co.appman.pokedex.view.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import th.co.appman.pokedex.base.BaseActivity
import th.co.appman.pokedex.base.Constants
import th.co.appman.pokedex.databinding.ActivityMainBinding
import th.co.appman.pokedex.models.Card
import th.co.appman.pokedex.models.PokemonModel
import th.co.appman.pokedex.view.pokemonlist.PokemonListActivity
import th.co.appman.pokedex.viewmodel.MainActivityViewModel

class MainActivity : BaseActivity(), MainActivityAdapter.OnItemClickListener {

    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var mainActivityAdapter: MainActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        observeData()
        initAdapter()
        init()
    }

    private fun init() {
        mainActivityViewModel.getPokemonList()

        mainBinding.imgAdd.setOnClickListener {
            mainActivityViewModel.getPokemonData()
        }
    }

    private fun observeData() {
        mainActivityViewModel.pokemonListLiveData.observe(this, Observer{
            mainActivityAdapter.setListData(it)
        })

        mainActivityViewModel.pokemonDataLiveData.observe(this, Observer{
            val intent = Intent(this, PokemonListActivity::class.java).apply {
                putExtra(POKEMON_LIST_DATA, it ?: null)
            }
            startActivityForResult(intent, POKEMON_LIST_CODE)
        })
    }

    private fun initAdapter() {
        mainActivityAdapter = MainActivityAdapter()
        mainActivityAdapter.setOnItemClickListener(this)

        mainBinding.recyclerViewPokemonList.apply {
            layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                else GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mainActivityAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            POKEMON_LIST_CODE -> {
                if (resultCode == PokemonListActivity.POKEMON_LIST_SUCCESS) {
                    data?.extras?.let {
                        val pokemonModel =
                            it.getParcelable<PokemonModel>(POKEMON_LIST_DATA)

                        mainActivityViewModel.setPokemonList(pokemonModel ?: PokemonModel())
                        mainActivityViewModel.getPokemonList()
                    }
                }
            }
            else -> {
                Log.e(TAG, Constants.REQUEST_CODE_NOT_MATCH)
            }
        }
    }

    companion object {
        const val POKEMON_LIST_CODE = 201
        const val POKEMON_LIST_DATA = "POKEMON_LIST_DATA"

        private const val TAG = "MainActivity"
    }

    override fun onItemClickDelete(mItem: Card) {
        mainActivityViewModel.changePokemonData(mItem)
    }
}
