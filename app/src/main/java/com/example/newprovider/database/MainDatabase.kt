package com.example.newprovider.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DeviceUser::class], version = 2)
abstract class MainDatabase : RoomDatabase() {
    abstract fun domainDao(): DeviceUserDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getInstance(context: Context): MainDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context, MainDatabase::class.java, "main").build()

                INSTANCE = instance
                instance
            }
        }
    }
}