package com.rifki.kotlin.mynotesapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.rifki.kotlin.mynotesapp.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.rifki.kotlin.mynotesapp.db.DatabaseContract.NoteColumns.Companion._ID
import java.sql.SQLException

class NoteHelper(context: Context){

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE : NoteHelper? = null

        //akan digunakan untuk menginisiasi database
        fun getInstance(context: Context) : NoteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NoteHelper(context)
            }

        private lateinit var database : SQLiteDatabase
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    //membuka dan menutup database
    @Throws(SQLException::class)
    fun open() {
        database  = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if(database.isOpen)
            database.close()
    }

    //Ambil data dari semua note yang ada di dalam database
    fun queryAll() : Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC")
    }

    //Ambil data berdasarkan _ID
    fun queryById(id : String) : Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    //insert data
    fun insert(values: ContentValues?) : Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    //update data
    fun update(id : String, values: ContentValues?) : Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    //delete data
    fun deleteById(id: String) : Int {
        return database.delete(DATABASE_TABLE,"$_ID = '$id'", null)
    }
}