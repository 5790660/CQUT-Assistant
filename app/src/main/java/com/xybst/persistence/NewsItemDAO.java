package com.xybst.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.xybst.bean.NewsItem;
import com.xybst.util.NewsType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 创宇 on 2016/1/2.
 */
public class NewsItemDAO {

    public static final String TB_NAME = "articleListDatabase";

    private SQLiteDatabase db;
    private List<NewsItem> items = new ArrayList<>();

    public NewsItemDAO(Context context) {
        ArticleListDBHelper dbHelper = new ArticleListDBHelper(context, TB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();
    }

    public boolean addArticleList(NewsType type, List<NewsItem> list) {
        boolean isSucceed = false;
        items.addAll(list);
        if (hasCache(type)) db.delete(TB_NAME, " type = ? ", new String[]{type.name()});
        for (NewsItem item : items) {
            ContentValues values = new ContentValues();
            values.put("type", type.name());
            values.put("title", item.getTitle());
            values.put("link ", item.getLink());
            values.put("publisher", item.getPublisher());
            values.put("timeTable", item.getTime());
            if (db.insert(TB_NAME, null, values) == -1)
                isSucceed = false;
        }
        return isSucceed;
    }

    public boolean hasCache(NewsType type) {
        String sql = "SELECT COUNT(*) FROM " + TB_NAME + " WHERE type = '"+ type.name() +"'";
        SQLiteStatement statement = db.compileStatement(sql);
        long count  =statement.simpleQueryForLong();
        Log.i("cache", String.valueOf(count));
        return count != 0;
    }

    public List<NewsItem> getArticleListItems(NewsType type) {
        Cursor cursor = db.query(TB_NAME, null, " type = ? ", new String[]{type.name()}, null, null, "_id ASC");
        try {
            List<NewsItem> items = new ArrayList<>();
            while (cursor.moveToNext()) {
                items.add(getItem(cursor));
            }
            System.out.println(items.size());
            return items;
        } finally {
            cursor.close();
        }
    }

    public NewsItem getItem(Cursor cursor) {
        NewsItem item = new NewsItem();
        item.setId(cursor.getInt(cursor.getColumnIndex(NewsItem.ID)));
        item.setType(NewsType.valueOf(cursor.getString(cursor.getColumnIndex(NewsItem.TYPE))));
        item.setTitle(cursor.getString(cursor.getColumnIndex(NewsItem.TITLE )));
        item.setLink(cursor.getString(cursor.getColumnIndex(NewsItem.LINK )));
        item.setPublisher(cursor.getString(cursor.getColumnIndex(NewsItem.PUBLISHER )));
        item.setTime(cursor.getString(cursor.getColumnIndex(NewsItem.TIME )));
        return item;
    }

    private class ArticleListDBHelper extends SQLiteOpenHelper {

        private ArticleListDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                   int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " +
                    TB_NAME + "("+ NewsItem.ID + " integer primary key autoincrement," +//
                    NewsItem.TYPE + " varchar,"+
                    NewsItem.TITLE +" varchar,"+
                    NewsItem.LINK +" varchar," +
                    NewsItem.PUBLISHER +" varchar,"+
                    NewsItem.TIME + " varchar"+
                    ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
            onCreate(db);
        }
    }
}
