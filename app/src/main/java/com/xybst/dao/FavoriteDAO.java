package com.xybst.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xybst.bean.ArticlesListItem;

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

    public boolean addFavoriteArticle(ArticlesListItem item) {
        boolean isSucceed = true;
        System.out.println("add   " + item.getTitle());
        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("link ", item.getLink());
        values.put("publisher", item.getPublisher());
        values.put("time", item.getTime());
        if (db.insert(TB_NAME, null, values) == -1)
            isSucceed = false;
        return isSucceed;
    }

    public List<ArticlesListItem> getFavoriteArticleList() {
        Cursor cursor = db.query(TB_NAME, null, null, null, null, null, "_id DESC");
        try {
            List<ArticlesListItem> items = new ArrayList<>();
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

    public ArticlesListItem getItem(Cursor cursor) {
        ArticlesListItem item = new ArticlesListItem();
        item.setId(cursor.getInt(cursor.getColumnIndex(ArticlesListItem.ID)));
        item.setModule(cursor.getString(cursor.getColumnIndex(ArticlesListItem.MODULE )));
        item.setTitle(cursor.getString(cursor.getColumnIndex(ArticlesListItem.TITLE )));
        item.setLink(cursor.getString(cursor.getColumnIndex(ArticlesListItem.LINK )));
        item.setPublisher(cursor.getString(cursor.getColumnIndex(ArticlesListItem.PUBLISHER )));
        item.setTime(cursor.getString(cursor.getColumnIndex(ArticlesListItem.TIME )));
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
                    TB_NAME + "("+ ArticlesListItem.ID + " integer primary key autoincrement," +//
                    ArticlesListItem.MODULE + " varchar,"+
                    ArticlesListItem.TITLE +" varchar,"+
                    ArticlesListItem.LINK +" varchar," +
                    ArticlesListItem.PUBLISHER +" varchar,"+
                    ArticlesListItem.TIME + " varchar"+
                    ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
            onCreate(db);
        }
    }
}
