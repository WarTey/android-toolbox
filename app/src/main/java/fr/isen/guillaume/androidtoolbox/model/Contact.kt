package fr.isen.guillaume.androidtoolbox.model

import android.net.Uri

data class Contact(var name: String, var number: String?, var email: String?, var picture: Uri)