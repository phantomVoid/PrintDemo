package net.printer.printdemo16.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.printer.printdemo16.pojo.XmlPojo;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version)
    {
        super(context, name, cursorFactory, version);
    }

    /*静态变量声明处*/
    public String DB_NAME = "void_ocr.db3";
    /*数据库的版本号*/
    private String DB_VERSION = "v0.0";
    /*表名字*/
    public String TABLE_NAME = "void_ocr";
    /*创建表语句*/
    private String OCR_TABLE_CREATE = "create table " + TABLE_NAME + " (car_area String, car_no String, in_time String);";
    /*删除表操作*/
    private String DROP_OCR_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO 创建数据库后，对数据库的操作
        db.execSQL(OCR_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 更改数据库版本的操作
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // TODO 每次成功打开数据库后首先被执行
    }
}
