package com.example.newprovider.database

import android.database.Cursor
import androidx.room.*
import com.example.newprovider.database.DeviceUser

@Dao
interface DeviceUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(domain: DeviceUser): Long

    @Update
    fun update(domain: DeviceUser): Int

    @Delete
    fun delete(domain: DeviceUser): Int

    @Query(value = "DELETE FROM DeviceUser WHERE id=:id")
    fun deleteById(id: Int): Int

    @Query(value = """SELECT * FROM DeviceUser ORDER BY CASE :order WHEN 1 THEN 'id ASC' ELSE 'id DESC' END""")
    fun selectAll(order: Boolean = true): Cursor

    @Query(value = "SELECT * FROM DeviceUser WHERE id=:id")
    fun selectById(id: Int): Cursor

}