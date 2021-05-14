package th.co.appman.pokedex.viewmodel

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import th.co.appman.pokedex.LiveDataTestUtil
import th.co.appman.pokedex.anyType
import th.co.appman.pokedex.models.PokemonModel
import th.co.appman.pokedex.repository.MainRepository

@Config(
    sdk = [Build.VERSION_CODES.O_MR1]
)
@RunWith(RobolectricTestRunner::class)
class PokemonListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var mContext: Context = ApplicationProvider.getApplicationContext()

    @Mock
    private lateinit var mainRepository: MainRepository

    private lateinit var vm: PokemonListViewModel

    private lateinit var pokemonModel: PokemonModel

    private val errorMessage = "Error message"

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        vm = PokemonListViewModel(mainRepository)

        val mockDataString =
            mContext.assets.open("mockPokemonList.json").bufferedReader().readText()
        pokemonModel = Gson().fromJson(mockDataString, PokemonModel::class.java)
    }

    @Test
    fun setPokemonDataListNotNull() {
        vm.setPokemonDataList(pokemonModel)

        val result = LiveDataTestUtil.getValue(vm.setPokemonSuccessLiveData)

        assertEquals(true, result?.cards?.size != 0)
    }

    @Test
    fun setPokemonDataListNull() {
        vm.setPokemonDataList(PokemonModel())

        val result = LiveDataTestUtil.getValue(vm.setPokemonSuccessLiveData)

        assertEquals(true, result?.cards?.size == 0)
    }

    @Test
    fun getPokemonDataListNotNull() {
        pokemonModel.cards.forEachIndexed { index, card ->
            if (index % 2 == 0) card.isSelected = true
        }

        vm.setPokemonDataList(pokemonModel)

        vm.getPokemonDataList()

        val result = LiveDataTestUtil.getValue(vm.pokemonListDataLiveData)

        assertEquals(15, result?.size)
    }

    @Test
    fun getPokemonDataListNull() {
        vm.getPokemonDataList()

        val result = LiveDataTestUtil.getValue(vm.pokemonListDataLiveData)

        assertEquals(0, result?.size)
    }

    @Test
    fun changePokemonDataNotNull() {
        vm.setPokemonDataList(pokemonModel)
        vm.changePokemonData(pokemonModel.cards[1])

        val result = vm.getPokemonModel()

        assertEquals(true, result.cards[1].isSelected)
    }

    @Test
    fun changePokemonDataNull() {
        vm.changePokemonData(pokemonModel.cards[0])

        val result = vm.getPokemonModel()

        assertEquals(0, result.cards.size)
    }

    @Test
    fun getPokemonListSuccess() {
        Mockito.`when`(mainRepository.getPokeList(anyType(), anyType())).thenAnswer {
            it.getArgument<(callBackSuccess: (PokemonModel)) -> Unit>(0).invoke(pokemonModel)
        }

        vm.getPokemonList()

        val result = LiveDataTestUtil.getValue(vm.pokemonListLiveData)
        val error = LiveDataTestUtil.getValue(vm.errorMessageLiveData)

        assertNotNull(result)
        assertEquals(null, error)
    }

    @Test
    fun getPokemonListFail() {
        Mockito.`when`(mainRepository.getPokeList(anyType(), anyType())).thenAnswer {
            it.getArgument<(callBackFail: (String)) -> Unit>(1).invoke(errorMessage)
        }

        vm.getPokemonList()

        val result = LiveDataTestUtil.getValue(vm.pokemonListLiveData)
        val error = LiveDataTestUtil.getValue(vm.errorMessageLiveData)

        assertEquals(null, result)
        assertEquals(errorMessage, error)
    }
}
