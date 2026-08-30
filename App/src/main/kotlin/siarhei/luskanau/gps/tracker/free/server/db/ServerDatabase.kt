package siarhei.luskanau.gps.tracker.free.server.db

import android.content.Context
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import siarhei.luskanau.gps.tracker.free.model.ServerEntity
import siarhei.luskanau.gps.tracker.free.model.ServerTypeConverter

@Database(entities = [ServerEntity::class], version = 1, exportSchema = false)
@ColumnTypeConverters(ServerTypeConverter::class)
abstract class ServerDatabase : RoomDatabase() {

    abstract fun serverDao(): ServerDao

    companion object {
        @Volatile
        private var instance: ServerDatabase? = null

        fun getInstance(context: Context): ServerDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ServerDatabase::class.java,
                    "server.db"
                ).build().also { instance = it }
            }
    }
}
