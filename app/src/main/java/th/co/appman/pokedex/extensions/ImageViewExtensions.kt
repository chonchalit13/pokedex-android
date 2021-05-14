package th.co.appman.pokedex.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import th.co.appman.pokedex.R

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .applyDefaultRequestOptions(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
        .load(url)
        .error(R.drawable.ic_broken)
        .into(this)
}