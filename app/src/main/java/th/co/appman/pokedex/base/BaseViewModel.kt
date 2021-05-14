package th.co.appman.pokedex.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import th.co.appman.pokedex.utils.SingleLiveData

open class BaseViewModel : ViewModel() {
    val loadingLiveData: SingleLiveData<Boolean> = SingleLiveData()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
}