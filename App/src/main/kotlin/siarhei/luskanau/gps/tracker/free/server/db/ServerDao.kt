package siarhei.luskanau.gps.tracker.free.server.db

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import siarhei.luskanau.gps.tracker.free.model.ServerEntity

@Dao
interface ServerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putServer(server: ServerEntity): Long

    @Query("SELECT * FROM ServerEntity WHERE _id = :rowId")
    suspend fun getServerByRowId(rowId: Long): ServerEntity?

    @Query("SELECT * FROM ServerEntity ORDER BY _id ASC")
    suspend fun getServers(): List<ServerEntity>
}
