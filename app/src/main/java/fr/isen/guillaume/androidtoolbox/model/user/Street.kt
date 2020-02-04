package fr.isen.guillaume.androidtoolbox.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Street(@SerializedName("number") @Expose var number: String, @SerializedName("name") @Expose var name: String)
