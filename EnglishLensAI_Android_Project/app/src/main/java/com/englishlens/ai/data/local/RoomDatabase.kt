package com.englishlens.ai.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val sessionId: String,
    val sessionType: String,
    val sessionTitle: String,
    val sessionStatus: String,
    val createdAt: Long,
    val updatedAt: Long,
    val mergedPassage: String?,
    val activeTemplate: String?
)

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions ORDER BY updatedAt DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity)

    @Delete
    suspend fun deleteSession(session: SessionEntity)
}

@Database(entities = [SessionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}
