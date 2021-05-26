package th.co.toei.pokedex.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import th.co.toei.pokedex.base.Constants
import th.co.toei.pokedex.models.PokemonModel
import th.co.toei.pokedex.network.EndpointInterface

class MainRepositoryImpl(private val apiService: EndpointInterface) : MainRepository {

    override fun getPokeList(
        callBackSuccess: (PokemonModel) -> Unit,
        callBackFail: (String) -> Unit
    ) {
        apiService.getPokemonList().enqueue(object : Callback<PokemonModel?> {
            override fun onResponse(call: Call<PokemonModel?>, response: Response<PokemonModel?>) {
                if (response.code() == 200) {
                    response.body()?.let {
                        callBackSuccess.invoke(it)
                    } ?: callBackFail.invoke(Constants.RESPONSE_BODY_NULL)
                } else {
                    callBackFail.invoke(response.message())
                }
            }

            override fun onFailure(call: Call<PokemonModel?>, t: Throwable) {
                callBackFail.invoke("${t.message}")
            }
        })
    }
}
