package fr.isen.guillaume.androidtoolbox.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Picture(@SerializedName("large") @Expose var large: String)
