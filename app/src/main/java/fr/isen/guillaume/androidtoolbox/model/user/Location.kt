package fr.isen.guillaume.androidtoolbox.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("street") @Expose var street: Street, @SerializedName("city") @Expose var city: String, @SerializedName(
        "state"
    ) @Expose var state: String
)