package fr.isen.guillaume.androidtoolbox.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Name(@SerializedName("first") @Expose var first: String, @SerializedName("last") @Expose var last: String)
