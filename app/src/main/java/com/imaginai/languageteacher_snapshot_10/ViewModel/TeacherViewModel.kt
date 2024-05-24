package com.imaginai.languageteacher.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.imaginai.languageteacher.DataAccess.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.imaginai.languageteacher.Model.Teacher

class TeacherViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(application, AppDatabase::class.java, "teachers").build()
    private val teacherDao = db.teacherDao()

    val teachers = MutableLiveData<List<Teacher>>()

    init {
        loadTeachers()
    }

    private fun loadTeachers() {
        viewModelScope.launch(Dispatchers.IO) {
            val allTeachers = teacherDao.getAllTeachers()
            withContext(Dispatchers.Main) {
                teachers.value = allTeachers
            }
        }
    }

    fun addTeacher(teacher: Teacher) {
        viewModelScope.launch(Dispatchers.IO) {
            teacherDao.insertTeacher(teacher)
            loadTeachers()
        }
    }


    fun deleteTeacher(teacher: Teacher) {
        viewModelScope.launch(Dispatchers.IO) {
            val count = teacherDao.getTeacherCount()
            teacherDao.deleteTeacher(teacher)
            loadTeachers()  
            val count2 = teacherDao.getTeacherCount()

            if(count==count2){
                Log.d("TeacherViewModel.deleteTeacher","Deletation failure, Remaining teachers count: $count")

            }
            else {
                Log.d(
                    "TeacherViewModel.deleteTeacher",
                    "Deletation succesfull, Remaining teachers count: $count2"
                )

            }
        }
    }

    fun clearAllTeachers() {
        viewModelScope.launch(Dispatchers.IO) {
            teacherDao.deleteAllTeachers()
            loadTeachers()
        }
    }


    fun getTeachers() {
        viewModelScope.launch(Dispatchers.IO) {
            val allTeachers = teacherDao.getAllTeachers()
            withContext(Dispatchers.Main) {
                teachers.value = allTeachers
            }
        }
    }



    fun fetchDataAndUpdateUI(updateUI: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
            }
        }
    }
}







