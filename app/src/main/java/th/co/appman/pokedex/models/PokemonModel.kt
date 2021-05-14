package th.co.appman.pokedex.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PokemonModel(
    @SerializedName("cards")
    var cards: MutableList<Card> = mutableListOf()
) : Parcelable

@Parcelize
data class Card(
    @SerializedName("attacks")
    val attacks: MutableList<Attack> = mutableListOf(),
    @SerializedName("hp")
    val hp: Int = 0,
    @SerializedName("id")
    val id: String = "",
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("isSelected")
    var isSelected: Boolean = false,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("rarity")
    val rarity: Int = 0,
    @SerializedName("weaknesses")
    val weaknesses: MutableList<Weaknesse> = mutableListOf()
) : Parcelable

@Parcelize
data class Attack(
    @SerializedName("cost")
    val cost: MutableList<String> = mutableListOf(),
    @SerializedName("damage")
    val damage: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("text")
    val text: String = ""
) : Parcelable

@Parcelize
data class Weaknesse(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("value")
    val value: String = ""
) : Parcelable
