package th.co.toei.pokedex.viewmodel

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
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import th.co.toei.pokedex.LiveDataTestUtil
import th.co.toei.pokedex.models.PokemonModel

@Config(
    sdk = [Build.VERSION_CODES.O_MR1]
)
@RunWith(RobolectricTestRunner::class)
class MainActivityViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var mContext: Context = ApplicationProvider.getApplicationContext()

    lateinit var vm: MainActivityViewModel
    lateinit var pokemonModel: PokemonModel

    @Before
    fun setup() {
        vm = MainActivityViewModel()

        val mockDataString =
            mContext.assets.open("mockPokemonList.json").bufferedReader().readText()
        pokemonModel = Gson().fromJson(mockDataString, PokemonModel::class.java)
    }

    @Test
    fun getPokemonDataNotNull() {
        vm.setPokemonList(pokemonModel)
        vm.getPokemonData()

        val result = LiveDataTestUtil.getValue(vm.pokemonDataLiveData)

        assertEquals(true, result?.cards?.size != 0)
    }

    @Test
    fun getPokemonDataNull() {
        vm.getPokemonData()

        val result = LiveDataTestUtil.getValue(vm.pokemonDataLiveData)

        assertEquals(true, result?.cards?.size == 0)
    }

    @Test
    fun getPokemonListNotNull() {
        pokemonModel.cards.forEachIndexed { index, card ->
            if (index % 2 == 0) card.isSelected = true
        }

        vm.setPokemonList(pokemonModel)
        vm.getPokemonList()

        val result = LiveDataTestUtil.getValue(vm.pokemonListLiveData)

        assertEquals(15, result?.size)
    }

    @Test
    fun getPokemonListNull() {
        vm.getPokemonList()

        val result = LiveDataTestUtil.getValue(vm.pokemonListLiveData)

        assertEquals(true, result?.size == 0)
    }

    @Test
    fun changePokemonDataNotNull() {
        vm.setPokemonList(pokemonModel)
        vm.changePokemonData(pokemonModel.cards[1])

        vm.getPokemonData()

        val result = LiveDataTestUtil.getValue(vm.pokemonDataLiveData)

        assertNotNull(result?.cards)
    }

    @Test
    fun changePokemonDataNull() {
        vm.changePokemonData(pokemonModel.cards[0])

        vm.getPokemonData()

        val result = LiveDataTestUtil.getValue(vm.pokemonDataLiveData)

        assertEquals(0, result?.cards?.size)
    }
}