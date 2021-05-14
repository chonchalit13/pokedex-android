package th.co.appman.pokedex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import th.co.appman.pokedex.base.BaseViewModel
import th.co.appman.pokedex.models.Card
import th.co.appman.pokedex.models.PokemonModel
import th.co.appman.pokedex.repository.MainRepository
import th.co.appman.pokedex.utils.SingleLiveData
import java.util.*

class PokemonListViewModel(private val mainRepository: MainRepository) : BaseViewModel() {

    private var pokemonModel = PokemonModel()

    val pokemonListLiveData: SingleLiveData<PokemonModel> = SingleLiveData()
    val pokemonListDataLiveData: MutableLiveData<MutableList<Card>> = MutableLiveData()
    val setPokemonSuccessLiveData: MutableLiveData<PokemonModel> = MutableLiveData()
    val pokemonSearchLiveData: MutableLiveData<MutableList<Card>> = MutableLiveData()

    fun setPokemonDataList(pokemonListData: PokemonModel) {
        pokemonModel = pokemonListData
        setPokemonSuccessLiveData.value = pokemonModel
    }

    fun getPokemonDataList() {
        val pokemonListResult = pokemonModel.cards.filterNot { it.isSelected }.toMutableList()
        pokemonListDataLiveData.value = pokemonListResult
    }

    fun changePokemonData(dataCard: Card) {
        pokemonModel.cards.find { it.name == dataCard.name }?.isSelected = true
    }

    fun getPokemonModel(): PokemonModel = pokemonModel

    fun getPokemonList() {
        loadingLiveData.value = true
        mainRepository.getPokeList({ response ->
            loadingLiveData.value = false
            pokemonListLiveData.value = response
        }, { messageError ->
            loadingLiveData.value = false
            errorMessageLiveData.value = messageError
        })
    }

    fun searchPokemon(pokemonName: String) {
        viewModelScope.launch {
            val pokemonSearchResult =
                pokemonModel.cards.filter { it.name.toLowerCase(Locale.ROOT).trim().contains(pokemonName) }
                    .filterNot { it.isSelected }.toMutableList()
            pokemonSearchLiveData.postValue(pokemonSearchResult)
        }
    }
}
