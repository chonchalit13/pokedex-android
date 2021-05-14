package th.co.appman.pokedex.network

import retrofit2.Call
import retrofit2.http.POST
import th.co.appman.pokedex.models.PokemonModel

interface EndpointInterface {
    @POST("v3/f9916417-f92e-478e-bfbc-c39e43f7c75b")
    fun getPokemonList(): Call<PokemonModel>
}
