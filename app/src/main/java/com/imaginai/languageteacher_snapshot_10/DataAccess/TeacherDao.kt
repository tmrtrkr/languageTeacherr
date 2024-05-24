package com.imaginai.languageteacher.DataAccess

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import com.imaginai.languageteacher.Model.Teacher

@Dao
interface TeacherDao {
    @Query("SELECT * FROM Teacher")
    fun getAllTeachers(): List<Teacher>

    @Insert
    fun insertTeacher(teacher: Teacher)

    @Delete
    fun deleteTeacher(teacher: Teacher)

    @Query("DELETE FROM Teacher")
    fun deleteAllTeachers()

    @Query("SELECT COUNT(*) FROM Teacher")
    fun getTeacherCount(): Int
}
