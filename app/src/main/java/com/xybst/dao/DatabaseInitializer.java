package com.xybst.dao;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseInitializer {
	
	public static void initialize(Context context, String fileName) {
		databaseFilePath = context.getCacheDir() + "/campus.db";
		System.out.println(databaseFilePath);
		File file = new File(databaseFilePath);
		if (file.exists()) {
			db = SQLiteDatabase.openDatabase(file.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
		}
		else {
			db = SQLiteDatabase.openOrCreateDatabase(file, null);
		}
	}	
	
	public static SQLiteDatabase getDatabase() {
		return db;
	}
	
	public static String getDatabaseFilePath() {
		return databaseFilePath;
	}
	
	private static String databaseFilePath = null;
	private static SQLiteDatabase db = null;

}
