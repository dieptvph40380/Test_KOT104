package com.example.test_kot104.room

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.test_kot104.viewmodel.SinhVienModel

@Database(entities = arrayOf(SinhVienModel::class), version = 1)
abstract class SinhVienDB :RoomDatabase(){
    abstract fun sinhvienDAO():SinhVienDao
}

@Dao
interface SinhVienDao{
    @Query("SELECT * FROM SinhVien")
    fun getAll():List<SinhVienModel>

    @Query("SELECT * FROM  SinhVien WHERE uid IN(:userIds)")
    fun loadAllByIds(userIds:IntArray):List<SinhVienModel>

    @Insert
    fun insert(vararg user:SinhVienModel)

    @Update
    fun update(user: SinhVienModel)

    @Delete
    fun delete(user: SinhVienModel)
}
