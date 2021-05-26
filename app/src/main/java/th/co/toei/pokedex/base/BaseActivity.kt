package th.co.toei.pokedex.base

import androidx.appcompat.app.AppCompatActivity
import th.co.toei.pokedex.alert.LoadingDialogFragment

open class BaseActivity : AppCompatActivity() {
    val alertLoading = LoadingDialogFragment.Builder().build()
}
