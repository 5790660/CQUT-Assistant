package com.xybst.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.xybst.bean.ArticlesListItem;
import com.xybst.bean.Grade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 创宇 on 2016/1/8.
 */
public class GradeDAO {

    private SQLiteDatabase db;

    public static final String TB_NAME = "Grade";

    public GradeDAO(Context context) {
        GradeDBHelper dbHelper = new GradeDBHelper(context, TB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();
    }

    public boolean addGrades(List<Grade> items) {
        db.delete(TB_NAME, null, null);
        boolean isSucceed = true;
        System.out.println("add   " + items.size());
        for (Grade item : items) {
            ContentValues values = new ContentValues();
            values.put(Grade.COURSENAME, item.getCourseName());
            values.put(Grade.CREDIT, item.getCredit());
            values.put(Grade.POINT, item.getPoint());
            values.put(Grade.SCORE, item.getScore());
            values.put(Grade.TERM, item.getTerm());
            values.put(Grade.YEAR, item.getYear());
            if (db.insert(TB_NAME, null, values) == -1)
                isSucceed = false;
        }
        return isSucceed;
    }

    public List<Grade> getGrades() {
        Cursor cursor = db.query(TB_NAME, null, null, null, null, null, "_id ASC");
        try {
            List<Grade> items = new ArrayList<>();
            while (cursor.moveToNext()) {
                items.add(getItem(cursor));
            }
            return items;
        } finally {
            cursor.close();
        }

    }

    public boolean hasCache() {
        String sql = "SELECT COUNT(*) FROM " + TB_NAME ;
        SQLiteStatement statement = db.compileStatement(sql);
        long count  =statement.simpleQueryForLong();
        Log.i("cache", String.valueOf(count));
        return count != 0;
    }

    public Grade getItem(Cursor cursor) {
        Grade item = new Grade();
        item.setCourseName(cursor.getString(cursor.getColumnIndex(Grade.COURSENAME)));
        item.setCredit(cursor.getString(cursor.getColumnIndex(Grade.CREDIT)));
        item.setPoint(cursor.getString(cursor.getColumnIndex(Grade.POINT)));
        item.setScore(cursor.getString(cursor.getColumnIndex(Grade.SCORE)));
        item.setTerm(cursor.getString(cursor.getColumnIndex(Grade.TERM)));
        item.setYear(cursor.getString(cursor.getColumnIndex(Grade.YEAR)));
        return item;
    }

    private static class GradeDBHelper extends SQLiteOpenHelper {
        public GradeDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + " (_id integer primary key autoincrement, "+
                    Grade.COURSENAME + " varchar," +
                    Grade.CREDIT + " varchar,"+
                    Grade.POINT + " varchar," +
                    Grade.SCORE + " varchar," +
                    Grade.TERM  + " varchar," +
                    Grade.YEAR + " varchar)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
            onCreate(db);
        }
    }
}
