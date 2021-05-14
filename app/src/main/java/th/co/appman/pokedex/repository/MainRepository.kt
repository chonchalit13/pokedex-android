package th.co.appman.pokedex.repository

import th.co.appman.pokedex.models.PokemonModel

interface MainRepository {
    fun getPokeList(callBackSuccess: (PokemonModel) -> Unit, callBackFail: (String) -> Unit)
}
