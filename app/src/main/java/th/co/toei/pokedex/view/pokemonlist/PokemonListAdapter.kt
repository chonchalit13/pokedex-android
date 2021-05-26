package th.co.toei.pokedex.view.pokemonlist

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.pokemon_list.view.*
import th.co.toei.pokedex.R
import th.co.toei.pokedex.extensions.loadImage
import th.co.toei.pokedex.models.Card

class PokemonListAdapter : RecyclerView.Adapter<PokemonListAdapter.ViewHolder>() {

    private var originalItems: MutableList<Card> = mutableListOf()
    private var mListener: OnItemClickListener? = null

    private val starRarity = String(Character.toChars(0x2B50))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(originalItems[position], position)
    }

    override fun getItemCount(): Int = originalItems.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(mItem: Card, position: Int) {
            var damage = 0
            var rarity = ""

            itemView.cardView.preventCornerOverlap =
                itemView.resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT

            mItem.attacks.forEach {
                damage += it.damage.toIntOrNull() ?: 0
            }

            val str = damage - mItem.weaknesses.size

            itemView.tvPokemonName.text = mItem.name
            itemView.progressHp.progress = mItem.hp
            itemView.progressStr.progress = str
            itemView.progressWeak.progress = mItem.weaknesses.size * 10

            for (i in 1..mItem.rarity) {
                rarity += starRarity
            }

            itemView.tvRarity.text = rarity
            itemView.imgPokemon.loadImage(mItem.imageUrl)
            itemView.tvAdd.setOnClickListener {
                originalItems.find { it.name == mItem.name }?.isSelected = true
                mListener?.onItemClickAdd(mItem)
                originalItems.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    fun setListData(response: MutableList<Card>) {
        originalItems.clear()
        originalItems.addAll(response)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClickAdd(mItem: Card)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }
}
