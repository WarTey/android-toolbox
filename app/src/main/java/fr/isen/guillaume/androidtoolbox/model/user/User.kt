package fr.isen.guillaume.androidtoolbox.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(@SerializedName("name") @Expose var name: Name, @SerializedName("location") @Expose var location: Location, @SerializedName("email") @Expose var email: String, @SerializedName("picture") @Expose var picture: Picture)