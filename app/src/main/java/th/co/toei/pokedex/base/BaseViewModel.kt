package th.co.toei.pokedex.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import th.co.toei.pokedex.utils.SingleLiveData

open class BaseViewModel : ViewModel() {
    val loadingLiveData: SingleLiveData<Boolean> = SingleLiveData()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
}