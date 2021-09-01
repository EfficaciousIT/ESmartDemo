package com.mobi.efficacious.ESmartDemo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by EFF-4 on 3/13/2018.
 */

public class Databasehelper extends SQLiteOpenHelper {

    public Databasehelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void query(String sql)
    {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
    }
    public Cursor querydata(String sql)
    {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        return sqLiteDatabase.rawQuery(sql,null);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
