package th.co.appman.pokedex.alert

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import th.co.appman.pokedex.R
import th.co.appman.pokedex.databinding.LayoutLoadingDialogBinding

class LoadingDialogFragment : DialogFragment() {

    private lateinit var loadingDialogBinding: LayoutLoadingDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_PokeDex)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialogBinding = LayoutLoadingDialogBinding.inflate(layoutInflater)
        return loadingDialogBinding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.let { wd ->
            wd.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            wd.setGravity(Gravity.CENTER)
        }
    }

    class Builder {
        fun build(): LoadingDialogFragment {
            return newInstance()
        }
    }

    companion object {
        fun newInstance(): LoadingDialogFragment = LoadingDialogFragment()
    }
}
