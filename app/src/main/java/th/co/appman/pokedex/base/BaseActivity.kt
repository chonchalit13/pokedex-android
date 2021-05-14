package th.co.appman.pokedex.base

import androidx.appcompat.app.AppCompatActivity
import th.co.appman.pokedex.alert.LoadingDialogFragment

open class BaseActivity : AppCompatActivity() {
    val alertLoading = LoadingDialogFragment.Builder().build()
}
