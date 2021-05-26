package th.co.toei.pokedex.network

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import junit.framework.Assert.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(
    sdk = [Build.VERSION_CODES.O_MR1]
)
@RunWith(RobolectricTestRunner::class)
class ApiServiceTest {

    private var mContext: Context = ApplicationProvider.getApplicationContext()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: EndpointInterface
    private lateinit var pokemonString: String

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = ApiService().buildService(
            EndpointInterface::class.java,
            mockWebServer.url("/").toString()
        )

        pokemonString = mContext.assets.open("mockPokemonList.json").bufferedReader().readText()
    }

    @Test
    fun getPokemonListSuccess() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(pokemonString))

        val response = apiService.getPokemonList().execute()

        assertEquals(200, response.code())
        assertNotNull(response.body())
    }

    @Test
    fun getPokemonListUnauthorized() {
        mockWebServer.enqueue(MockResponse().setResponseCode(401))

        val response = apiService.getPokemonList().execute()

        assertEquals(401, response.code())
        assertNull(response.body())
    }

    @Test
    fun getPokemonListInternalServerError() {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        val response = apiService.getPokemonList().execute()

        assertEquals(500, response.code())
        assertNull(response.body())
    }

    @Test
    fun getPokemonListUrlDefault() {
        apiService = ApiService().buildService(EndpointInterface::class.java)

        val response = apiService.getPokemonList().execute()

        assertEquals(200, response.code())
        assertNotNull(response.body())
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}