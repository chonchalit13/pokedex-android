package th.co.appman.pokedex.repository

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import th.co.appman.pokedex.base.Constants
import th.co.appman.pokedex.models.PokemonModel
import th.co.appman.pokedex.network.EndpointInterface

@Config(
    sdk = [Build.VERSION_CODES.O_MR1]
)
@RunWith(RobolectricTestRunner::class)
class MainRepositoryImplTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var callPokemonModel: Call<PokemonModel>

    private var mContext: Context = ApplicationProvider.getApplicationContext()
    private val apiService: EndpointInterface by lazy { Mockito.mock(EndpointInterface::class.java) }
    private lateinit var mainRepositoryImpl: MainRepositoryImpl
    private lateinit var pokemonModel: PokemonModel
    private val errorMessage = "Error message"

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        mainRepositoryImpl = MainRepositoryImpl(apiService)

        val mockDataString =
            mContext.assets.open("mockPokemonList.json").bufferedReader().readText()
        pokemonModel = Gson().fromJson(mockDataString, PokemonModel::class.java)

        Mockito.`when`(apiService.getPokemonList()).thenReturn(callPokemonModel)
    }

    @Test
    fun getPokeListSuccess() {
        Mockito.doAnswer {
            val callBack: Callback<PokemonModel> = it.getArgument(0)
            callBack.onResponse(callPokemonModel, Response.success(200, pokemonModel))
        }.`when`(callPokemonModel).enqueue(Mockito.any())

        mainRepositoryImpl.getPokeList({ result ->
            assertNotNull(result)
        }, { })
    }

    @Test
    fun getPokeListSuccessResponseNull() {
        val mockPokemonModelNull: PokemonModel? = null

        Mockito.doAnswer {
            val callBack: Callback<PokemonModel> = it.getArgument(0)
            callBack.onResponse(callPokemonModel, Response.success(200, mockPokemonModelNull))
        }.`when`(callPokemonModel).enqueue(Mockito.any())

        mainRepositoryImpl.getPokeList({ }, { messageError ->
            assertEquals(Constants.RESPONSE_BODY_NULL, messageError)
        })
    }

    @Test
    fun getPokeListUnSuccess() {
        Mockito.doAnswer {
            val callBack: Callback<PokemonModel> = it.getArgument(0)
            callBack.onResponse(
                callPokemonModel,
                Response.error(500, errorMessage.toResponseBody())
            )
        }.`when`(callPokemonModel).enqueue(Mockito.any())

        mainRepositoryImpl.getPokeList({ }, { messageError ->
            assertNotNull(messageError)
        })
    }

    @Test
    fun getPokeListFail() {
        Mockito.doAnswer {
            val callBack: Callback<PokemonModel> = it.getArgument(0)
            callBack.onFailure(callPokemonModel, Exception(errorMessage))
        }.`when`(callPokemonModel).enqueue(Mockito.any())

        mainRepositoryImpl.getPokeList({ }, { messageError ->
            assertEquals(errorMessage, messageError)
        })
    }
}
