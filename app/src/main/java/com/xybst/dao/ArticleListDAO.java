package com.xybst.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.xybst.bean.ArticlesListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 创宇 on 2016/1/2.
 */
public class ArticleListDAO {

    public static final String TB_NAME = "articleListDatabase";

    private SQLiteDatabase db;
    private List<ArticlesListItem> items = new ArrayList<>();

    public ArticleListDAO(Context context) {
        ArticleListDBHelper dbHelper = new ArticleListDBHelper(context, TB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();
    }

    public boolean addArticleList(String module, List<ArticlesListItem> list) {
        boolean isSucceed = false;
        items.addAll(list);
        if (hasCache(module)) db.delete(TB_NAME, " module = ? ", new String[]{module});
        for (ArticlesListItem item : items) {
            ContentValues values = new ContentValues();
            values.put("module", module);
            values.put("title", item.getTitle());
            values.put("link ", item.getLink());
            values.put("publisher", item.getPublisher());
            values.put("time", item.getTime());
            if (db.insert(TB_NAME, null, values) == -1)
                isSucceed = false;
        }
        return isSucceed;
    }

    public boolean hasCache(String module) {
        String sql = "SELECT COUNT(*) FROM " + TB_NAME + " WHERE module = '"+module+"'";
        SQLiteStatement statement = db.compileStatement(sql);
        long count  =statement.simpleQueryForLong();
        Log.i("cache", String.valueOf(count));
        return count != 0;
    }

    public List<ArticlesListItem> getArticleListItems(String module) {
        Cursor cursor = db.query(TB_NAME, null, " module = ? ", new String[]{module}, null, null, "_id ASC");
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
                    TB_NAME + "("+ArticlesListItem.ID + " integer primary key autoincrement," +//
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
