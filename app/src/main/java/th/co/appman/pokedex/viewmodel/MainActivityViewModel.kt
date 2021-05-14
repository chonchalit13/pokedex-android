package th.co.appman.pokedex.viewmodel

import androidx.lifecycle.MutableLiveData
import th.co.appman.pokedex.base.BaseViewModel
import th.co.appman.pokedex.models.Card
import th.co.appman.pokedex.models.PokemonModel
import th.co.appman.pokedex.utils.SingleLiveData

class MainActivityViewModel : BaseViewModel() {

    private var pokemonModel = PokemonModel()

    val pokemonDataLiveData: SingleLiveData<PokemonModel> = SingleLiveData()
    val pokemonListLiveData: MutableLiveData<MutableList<Card>> = MutableLiveData()

    fun setPokemonList(pokemonListData: PokemonModel) {
        pokemonModel = pokemonListData
    }

    fun getPokemonList() {
        val pokemonListResult = pokemonModel.cards.filter { it.isSelected }.toMutableList()
        pokemonListLiveData.postValue(pokemonListResult)
    }

    fun getPokemonData() {
        pokemonDataLiveData.postValue(pokemonModel)
    }

    fun changePokemonData(dataCard: Card) {
        pokemonModel.cards.find { it.name == dataCard.name }?.isSelected = false
    }
}
