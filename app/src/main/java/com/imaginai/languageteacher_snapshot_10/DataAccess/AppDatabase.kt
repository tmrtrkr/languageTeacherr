package com.imaginai.languageteacher.DataAccess

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.imaginai.languageteacher.Model.Teacher

@Database(entities = [Teacher::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teacherDao(): TeacherDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "teacher_database"
                )
                    .fallbackToDestructiveMigration() 
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            db.execSQL("DELETE FROM Teacher")
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
