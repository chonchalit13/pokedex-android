package th.co.toei.pokedex.alert

import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import th.co.toei.pokedex.R
import th.co.toei.pokedex.databinding.LayoutAlertDialogBinding

class AlertMessageDialogFragment : DialogFragment() {

    private lateinit var alertDialogBinding: LayoutAlertDialogBinding

    private var callback: (() -> Unit)? = null
    private var text = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_PokeDex)
        if (savedInstanceState == null) {
            restoreArguments(arguments)
        } else {
            restoreInstanceState(savedInstanceState)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        dialog?.window?.setBackgroundDrawableResource((R.drawable.bg_alert_dialog))
        alertDialogBinding = LayoutAlertDialogBinding.inflate(layoutInflater)
        return alertDialogBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            text = savedInstanceState.getString(TEXT, "")
        }
        init()
    }

    private fun init() {
        alertDialogBinding.tvMessage.text = text
        alertDialogBinding.btnConfirm.setOnClickListener {
            callback?.invoke()
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.let { wd ->
            val size = Point()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                wd.windowManager.currentWindowMetrics
            } else {
                wd.windowManager.defaultDisplay.getSize(size)
            }

            val width = size.x

            wd.setLayout((width * 0.9).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            wd.setGravity(Gravity.CENTER)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT, text)
    }

    private fun restoreInstanceState(bundle: Bundle) {
        text = bundle.getString(TEXT, "")
    }

    private fun restoreArguments(bundle: Bundle?) {
        text = bundle?.getString(TEXT, "") ?: ""
    }

    class Builder {
        private lateinit var text: String
        private var callback: (() -> Unit)? = null

        fun setMessage(text: String): Builder {
            this.text = text
            return this
        }

        fun setCallback(callback: () -> Unit): Builder {
            this.callback = callback
            return this
        }

        fun build(): AlertMessageDialogFragment {
            return newInstance(text, callback)
        }
    }

    companion object {
        private const val TEXT = "text"

        fun newInstance(text: String, callback: (() -> Unit)?): AlertMessageDialogFragment {
            val fragment = AlertMessageDialogFragment()
            val bundle = Bundle()
            bundle.putString(TEXT, text)
            fragment.arguments = bundle
            fragment.callback = callback
            return fragment
        }
    }
}
