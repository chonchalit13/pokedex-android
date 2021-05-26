package th.co.toei.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import th.co.toei.pokedex.repository.MainRepository

class PokemonListViewModelFactory(private val mainRepository: MainRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MainRepository::class.java)
            .newInstance(mainRepository)
    }
}
