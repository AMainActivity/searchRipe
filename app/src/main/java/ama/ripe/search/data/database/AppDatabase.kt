package ama.ripe.search.data.database

import ama.ripe.search.data.database.AppDatabase.Companion.DB_VERSION
import ama.ripe.search.data.database.models.InetNumDbModel
import ama.ripe.search.data.database.models.IpOrganization
import ama.ripe.search.data.database.models.OrganizationDbModel
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        OrganizationDbModel::class,
        InetNumDbModel::class,
        IpOrganization::class
    ],
    version = DB_VERSION,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    companion object {

        private var db: AppDatabase? = null
        private const val DB_NAME = "main.db"
        const val DB_VERSION = 1
        private val LOCK = Any()

        fun getInstance(context: Context): AppDatabase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        // .allowMainThreadQueries()
                        .build()
                db = instance
                return instance
            }
        }
    }

    abstract fun orgObjectDao(): OrgObjectDao
    abstract fun inetNumObjectDao(): InetNumObjectDao
    abstract fun ipDao(): IpDao
}
