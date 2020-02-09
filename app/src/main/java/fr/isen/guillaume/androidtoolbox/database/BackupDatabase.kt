package fr.isen.guillaume.androidtoolbox.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.isen.guillaume.androidtoolbox.database.dao.BackupDao
import fr.isen.guillaume.androidtoolbox.model.BackupRoom

@Database(entities = [BackupRoom::class], version = 1)
abstract class BackupDatabase : RoomDatabase() {

    abstract fun backupDao(): BackupDao

    companion object {
        private var INSTANCE: BackupDatabase? = null

        fun getInstance(context: Context): BackupDatabase? {
            if (INSTANCE == null)
                synchronized(BackupDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        BackupDatabase::class.java,
                        "backup.db"
                    ).build()
                }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}