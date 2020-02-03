package fr.isen.guillaume.androidtoolbox.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.isen.guillaume.androidtoolbox.model.BackupRoom

@Dao
interface BackupDao {

    @Query("SELECT * FROM backup ORDER BY id DESC LIMIT 1")
    fun getLastUser(): BackupRoom

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: BackupRoom)
}