package codes.malukimuthusi.hackathon.database

import android.content.Context
import androidx.room.*

@Entity
data class Point(
    @PrimaryKey val id: Int,
    val lat: Double?,
    val lon: Double?
)

@Dao
interface PointDao {
    @Query("SELECT * FROM Point LIMIT 1")
    suspend fun fetchSavedPoint(): Point?
}

@Database(entities = [Point::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract val Dao: PointDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        /*
        * Create database.
        *
        * Only creates one instance of the database.
        * */
        fun getDatabase(context: Context): AppDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDb::class.java,
                        "AppDb"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}