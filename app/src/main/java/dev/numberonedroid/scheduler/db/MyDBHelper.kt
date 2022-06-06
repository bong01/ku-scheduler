package dev.numberonedroid.scheduler.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import dev.numberonedroid.scheduler.model.MyData


class MyDBHelper(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        val DB_NAME = "mysch.db"
        val DB_VERSION = 1
        val TABLE_NAME = "schedule"
        val ID = "id"
        val YEAR = "year"
        val MONTH = "month"
        val DAY = "day"
        val TITLE = "title"
        val CONTENT = "content"
        val STARTHOUR = "starthour"
        val STARTMIN = "startmin"
        val ENDHOUR = "endhour"
        val ENDMIN = "endmin"
        val IS_DONE = "isDone"
    }

    fun insertSchedule(data: MyData): Boolean {
        val values = ContentValues()
        values.put(ID, data.id)
        values.put(YEAR, data.year)
        values.put(MONTH, data.month)
        values.put(DAY, data.day)
        values.put(TITLE, data.title)
        values.put(CONTENT, data.content)
        values.put(STARTHOUR, data.starthour)
        values.put(STARTMIN, data.startmin)
        values.put(ENDHOUR, data.endhour)
        values.put(ENDMIN, data.endmin)
        values.put(IS_DONE, data.isDone)
        val db = writableDatabase
        if (db.insert(TABLE_NAME, null, values) > 0) {
            db.close() //성공하든 실패하든 무조건 데이터베이스는 닫기
            return true
        } else {
            db.close()
            return false
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists $TABLE_NAME(" +
                "$ID integer primary key autoincrement," +
                "$YEAR integer," +
                "$MONTH integer," +
                "$DAY integer, " +
                "$TITLE text, " +
                "$CONTENT text, " +
                "$STARTHOUR integer, " +
                "$STARTMIN integer," +
                "$ENDHOUR integer," +
                "$ENDMIN integer, " +
                "$IS_DONE varchar(10));" //table 생성
        db!!.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }


    @SuppressLint("Range")
    fun showSchedule(year: Int, month: Int, day: Int): ArrayList<MyData> {
        val mydata = arrayListOf<MyData>()
        val strsql =
            "select * from $TABLE_NAME where $YEAR='$year' and $MONTH='$month' and $DAY='$day' order by $STARTHOUR,$STARTMIN,$ENDHOUR,$ENDMIN,$ID;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        //val flag = cursor.count!=0
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(ID))
            val year = cursor.getInt(cursor.getColumnIndex(YEAR))
            val month = cursor.getInt(cursor.getColumnIndex(MONTH))
            val day = cursor.getInt(cursor.getColumnIndex(DAY))
            val title = cursor.getString(cursor.getColumnIndex(TITLE))
            val content = cursor.getString(cursor.getColumnIndex(CONTENT))
            val starthour = cursor.getInt(cursor.getColumnIndex(STARTHOUR))
            val startmin = cursor.getInt(cursor.getColumnIndex(STARTMIN))
            val endhour = cursor.getInt(cursor.getColumnIndex(ENDHOUR))
            val endmin = cursor.getInt(cursor.getColumnIndex(ENDMIN))
            val isDone = cursor.getString(cursor.getColumnIndex(IS_DONE)).toBoolean()

            mydata.add(MyData(id, year, month, day, title, content, starthour, startmin, endhour, endmin, isDone))
        }
        cursor.close()
        db.close()
        return mydata
    }

    fun deleteSchedule(id: Int) {
        val strsql = "delete from $TABLE_NAME where $ID='$id';"
        val db = writableDatabase
        db.execSQL(strsql)
        db.close()
    }

    fun updateSchedule(data: MyData): Boolean {
        val id = data.id
        val strsql = "select * from $TABLE_NAME where $ID='$id';" //따로 식별할 수 있는 아이디 필요
        val db = writableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count != 0
        if (flag) {
            cursor.moveToFirst()
            val values = ContentValues()
            values.put(YEAR, data.year)
            values.put(MONTH, data.month)
            values.put(DAY, data.day)
            values.put(TITLE, data.title)
            values.put(CONTENT, data.content)
            values.put(STARTHOUR, data.starthour)
            values.put(STARTMIN, data.startmin)
            values.put(ENDHOUR, data.endhour)
            values.put(ENDMIN, data.endmin)
            values.put(IS_DONE, data.isDone.toString())
            db.update(TABLE_NAME, values, "$ID=?", arrayOf(id.toString()))
        }
        cursor.close()
        db.close()
        return flag
    }

    @SuppressLint("Range")
    fun showSchedule2(year: Int, month: Int): ArrayList<MyData> {
        val mydata = arrayListOf<MyData>()
        val strsql = "select * from $TABLE_NAME where $YEAR='$year' and $MONTH='$month';"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        //val flag = cursor.count!=0
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(ID))
            val year = cursor.getInt(cursor.getColumnIndex(YEAR))
            val month = cursor.getInt(cursor.getColumnIndex(MONTH))
            val day = cursor.getInt(cursor.getColumnIndex(DAY))
            val title = cursor.getString(cursor.getColumnIndex(TITLE))
            val content = cursor.getString(cursor.getColumnIndex(CONTENT))
            val starthour = cursor.getInt(cursor.getColumnIndex(STARTHOUR))
            val startmin = cursor.getInt(cursor.getColumnIndex(STARTMIN))
            val endhour = cursor.getInt(cursor.getColumnIndex(ENDHOUR))
            val endmin = cursor.getInt(cursor.getColumnIndex(ENDMIN))
            val isDone = cursor.getString(cursor.getColumnIndex(IS_DONE)).toBoolean()

            mydata.add(MyData(id, year, month, day, title, content, starthour, startmin, endhour, endmin, isDone))
        }
        cursor.close()
        db.close()
        return ArrayList(mydata.sortedWith(compareBy({ it.day }, { it.starthour })))

    }
}

