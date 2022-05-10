package com.example.cs330_dz06.ui.main

import androidx.lifecycle.ViewModel
import com.example.cs330_dz06.data.db.DatabaseHandler
import com.example.cs330_dz06.data.model.Exam
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  val databaseHandler: DatabaseHandler
) :ViewModel() {



    fun addExam(exam: Exam, ): Long {

            return databaseHandler.addStudent(exam)
        }


    fun deleteExam(exam: Exam): Int {
        return databaseHandler.deleteStudent(exam)
    }

    fun getItemsList(): List<Exam> {

        return databaseHandler.viewStudent()
    }

    fun editExam(exam: Exam): Int {
        return databaseHandler.updateStudent(exam)
    }

    }
