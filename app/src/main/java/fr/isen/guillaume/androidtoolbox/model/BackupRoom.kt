package fr.isen.guillaume.androidtoolbox.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "backup")
data class BackupRoom(@PrimaryKey(autoGenerate = true) var id: Long?, @ColumnInfo(name = "name") var name: String, @ColumnInfo(name = "firstname") var firstname: String, @ColumnInfo(name = "birthday") var birthday: String) {

    constructor() : this(null,"","","")
}