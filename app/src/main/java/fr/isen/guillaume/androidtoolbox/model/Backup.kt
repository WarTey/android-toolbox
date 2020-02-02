package fr.isen.guillaume.androidtoolbox.model

import java.util.*

data class Backup(var name: String, var firstname: String, var birthday: String) {

    fun getAge(): Int {
        val birthday = birthday.split("-")
        if (birthday[1].toInt() > Calendar.getInstance()[Calendar.MONTH] + 1 || (birthday[1].toInt() == Calendar.getInstance()[Calendar.MONTH] + 1 && birthday[0].toInt() > Calendar.getInstance()[Calendar.DAY_OF_MONTH]))
            return Calendar.getInstance()[Calendar.YEAR] - birthday[2].toInt() - 1
        return Calendar.getInstance()[Calendar.YEAR] - birthday[2].toInt()
    }
}