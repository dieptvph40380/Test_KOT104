package com.example.test_kot104.viewmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SinhVien")
data class SinhVienModel(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "hoten") var hoten: String?,
    @ColumnInfo(name = "diem") var diem: Float?,
    @ColumnInfo(name = "statusSv") var statusSv: Boolean?,
    @ColumnInfo(name = "anh") var anh: String ?= null

)