package com.xybst.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xybst.bean.NewsItem;
import com.xybst.util.NewsType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 创宇 on 2016/3/15.
 */
public class FavoriteDAO {

    private SQLiteDatabase db;

    public static final String TB_NAME = "favorite";

    public FavoriteDAO(Context context) {
        ArticleListDBHelper dbHelper = new ArticleListDBHelper(context, TB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();
    }

    public boolean addFavoriteArticle(NewsItem item) {
        boolean isSucceed = true;
        System.out.println("add   " + item.getTitle());
        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("link ", item.getLink());
        values.put("publisher", item.getPublisher());
        values.put("timeTable", item.getTime());
        if (db.insert(TB_NAME, null, values) == -1)
            isSucceed = false;
        return isSucceed;
    }

    public List<NewsItem> getFavoriteArticleList() {
        Cursor cursor = db.query(TB_NAME, null, null, null, null, null, "_id DESC");
        try {
            List<NewsItem> items = new ArrayList<>();
            while (cursor.moveToNext()) {
                items.add(getItem(cursor));
            }
            return items;
        } finally {
            cursor.close();
        }
    }

    public boolean unFavorite(String url) {
        if (db.delete(TB_NAME, "link = ?", new String[]{url}) == 0)
            return false;
        return true;
    }

    public boolean isFavorite(String url) {
        Cursor c = db.rawQuery("select * from favorite where link = ?", new String[]{url});
        if (c.getCount() == 0)
            return false;
        return true;
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

    public class ArticleListDBHelper extends SQLiteOpenHelper {

        public ArticleListDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
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
