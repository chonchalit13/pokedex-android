package th.co.toei.pokedex.repository

import th.co.toei.pokedex.models.PokemonModel

interface MainRepository {
    fun getPokeList(callBackSuccess: (PokemonModel) -> Unit, callBackFail: (String) -> Unit)
}
