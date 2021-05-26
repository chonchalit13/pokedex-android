package th.co.toei.pokedex.view.pokemonlist

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import th.co.toei.pokedex.alert.AlertMessageDialogFragment
import th.co.toei.pokedex.base.BaseActivity
import th.co.toei.pokedex.databinding.ActivityPokemonListBinding
import th.co.toei.pokedex.models.Card
import th.co.toei.pokedex.models.PokemonModel
import th.co.toei.pokedex.network.ApiService
import th.co.toei.pokedex.network.EndpointInterface
import th.co.toei.pokedex.repository.MainRepositoryImpl
import th.co.toei.pokedex.viewmodel.PokemonListViewModel
import th.co.toei.pokedex.viewmodel.PokemonListViewModelFactory

class PokemonListActivity : BaseActivity(), PokemonListAdapter.OnItemClickListener {

    private val apiService =
        ApiService().buildService(EndpointInterface::class.java)

    private lateinit var pokemonListBinding: ActivityPokemonListBinding

    private lateinit var pokemonListViewModel: PokemonListViewModel
    private val pokemonListViewModelFactory =
        PokemonListViewModelFactory(MainRepositoryImpl(apiService))

    private var pokemonModel = PokemonModel()
    private lateinit var pokemonListAdapter: PokemonListAdapter
    private val resultIntent = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemonListBinding = ActivityPokemonListBinding.inflate(layoutInflater)
        setContentView(pokemonListBinding.root)

        pokemonListBinding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rec = Rect()
            pokemonListBinding.root.getWindowVisibleDisplayFrame(rec)
            val screenHeight = pokemonListBinding.root.rootView.rootView.height
            val keypadHeight = screenHeight - rec.bottom
            if (keypadHeight < screenHeight * 0.15) {
                pokemonListBinding.searchView.clearFocus()
            }
        }

        pokemonListViewModel = ViewModelProvider(
            this,
            pokemonListViewModelFactory
        ).get(PokemonListViewModel::class.java)

        if (savedInstanceState == null) {
            intent.extras?.let {
                pokemonModel = it.getParcelable(POKEMON_LIST_DATA) ?: PokemonModel()
            }
        } else {
            pokemonModel =
                savedInstanceState.getParcelable(POKEMON_LIST_DATA) ?: PokemonModel()
        }

        init()
        initAdapter()
        observeData()
    }

    private fun init() {
        if (pokemonModel.cards.size <= 0) {
            pokemonListViewModel.getPokemonList()
        } else {
            pokemonListViewModel.setPokemonDataList(pokemonModel)
        }

        pokemonListBinding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(inputText: String?): Boolean {
                inputText?.let {
                    pokemonListViewModel.searchPokemon(it)
                }
                pokemonListBinding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(inputText: String?): Boolean {
                inputText?.let {
                    pokemonListViewModel.searchPokemon(it)
                }
                return true
            }
        })
    }

    private fun observeData() {
        pokemonListViewModel.setPokemonSuccessLiveData.observe(this, Observer {
            pokemonListViewModel.getPokemonDataList()

            resultIntent.putExtra(POKEMON_LIST_DATA, it)
        })

        pokemonListViewModel.pokemonListLiveData.observe(this, Observer {
            pokemonListViewModel.setPokemonDataList(it)
        })

        pokemonListViewModel.pokemonListDataLiveData.observe(this, Observer {
            pokemonListAdapter.setListData(it)
        })

        pokemonListViewModel.errorMessageLiveData.observe(this, Observer {
            AlertMessageDialogFragment.Builder()
                .setMessage(it)
                .build()
                .show(supportFragmentManager, TAG)
        })

        pokemonListViewModel.loadingLiveData.observe(this, Observer { showLoading ->
            if (showLoading) {
                alertLoading.show(supportFragmentManager, TAG)
            } else {
                alertLoading.dismiss()
            }
        })

        pokemonListViewModel.pokemonSearchLiveData.observe(this, Observer {
            pokemonListAdapter.setListData(it)
        })
    }

    private fun initAdapter() {
        pokemonListAdapter = PokemonListAdapter()
        pokemonListAdapter.setOnItemClickListener(this)

        pokemonListBinding.recyclerViewPokemonList.apply {
            layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                else GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = pokemonListAdapter
        }
    }

    override fun onItemClickAdd(mItem: Card) {
        pokemonListViewModel.changePokemonData(mItem)
    }

    override fun onBackPressed() {
        setResult(POKEMON_LIST_SUCCESS, resultIntent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        if (!alertLoading.isCancelable) alertLoading.dismiss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(POKEMON_LIST_DATA, pokemonListViewModel.getPokemonModel())
    }

    companion object {
        const val POKEMON_LIST_DATA = "POKEMON_LIST_DATA"
        const val POKEMON_LIST_SUCCESS = 111

        private const val TAG = "PokemonListActivity"
    }
}
