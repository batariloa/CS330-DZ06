package com.example.cs330_dz06.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.cs330_dz06.data.model.Exam

class DatabaseHandler(context: Context) :
SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    companion object{
        private const val DATABASE_NAME = "ExamDatabase"
        private const val DATABASE_VERSION = 5
        private const val TABLE_STUDENTS = "ExamTable"

        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_CODE = "exam_code"
    }

    override fun onCreate(db: SQLiteDatabase?) {
    //create table with fields

        db?.execSQL("CREATE TABLE $TABLE_STUDENTS  ($KEY_ID INTEGER PRIMARY KEY NOT NULL, $KEY_CODE TEXT, $KEY_NAME TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
    }

    fun addStudent(exam: Exam):Long{
        //get db from helper context
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, exam.name)
        contentValues.put(KEY_CODE, exam.code)

        //insert row
        val success = db.insert(TABLE_STUDENTS, null, contentValues)
        close()

        return success
    }

    fun viewStudent(): List<Exam>{

        val studentList = mutableListOf<Exam>()

        val selectQuery = "SELECT * FROM $TABLE_STUDENTS"

        //database instance
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        }
        catch (e:SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }



        if(cursor.moveToFirst()){
            do{
                val student = Exam(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_CODE))
                )

                studentList.add(student)

            } while (cursor.moveToNext())
        }

        return studentList
    }

    fun updateStudent(exam: Exam): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, exam.name)
        contentValues.put(KEY_CODE, exam.code)

        //update where the ID is the same as passed exams's ID
        val success = db.update(TABLE_STUDENTS, contentValues, "$KEY_ID = ${exam.id}", null)


        return success
    }

    fun deleteStudent(exam: Exam): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, exam.name)
        contentValues.put(KEY_CODE, exam.code)

        //delete where the ID is the same as passed exam's ID
        val success = db.delete(TABLE_STUDENTS, "$KEY_ID = ${exam.id}", null)
        db.close()

        return success

    }
}
