package com.atasozlerivedeyimlersozlugu;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    int id=0;

    private static final String DATABASE_NAME   = "database";
    private static final String TABLE_ATASOZLERI = "atasozleri";
    private static final String TABLE_DEYIMLER = "deyimler";
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_ATASOZLERI + "(id INTEGER PRIMARY KEY,icerik TEXT)";
        String sql2 = "CREATE TABLE " + TABLE_DEYIMLER+ "(id INTEGER PRIMARY KEY,icerik TEXT)";
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATASOZLERI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEYIMLER);
        onCreate(db);
    }

    public void AtasozuEkle(Atasozu yeni) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",id++);
        values.put("icerik", yeni.getIcerik());
        db.insert(TABLE_ATASOZLERI, null, values);
        db.close();
    }
    public void DeyimEkle(Deyim yeni) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",id++);
        values.put("icerik", yeni.getIcerik());
        db.insert(TABLE_DEYIMLER, null, values);
        db.close();
    }

    public List<Atasozu> getAtasozleri() {
        List<Atasozu> atasozleri = new ArrayList<Atasozu>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_ATASOZLERI, new String[]{"id", "icerik"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Atasozu atasozu = new Atasozu();
            atasozu.setId(cursor.getInt(0));
            atasozu.setIcerik(cursor.getString(1));
            atasozleri.add(atasozu);
        }

        return atasozleri;
    }

    public List<Atasozu>AtasozuArama(String aranan){
        List<Atasozu> atasozleri = new ArrayList<Atasozu>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlQuery = "SELECT  * FROM " + TABLE_ATASOZLERI +" WHERE icerik LIKE '"+aranan+"%' OR icerik LIKE '% "+aranan+"%'";
        //String sqlQuery = "SELECT  * FROM " + TABLE_ATASOZLERI +" WHERE CONTAINS(icerik,'"+aranan+"')";
        Cursor cursor = db.rawQuery(sqlQuery, null);

        while (cursor.moveToNext()) {
            Atasozu atasozu = new Atasozu();
            atasozu.setId(cursor.getInt(0));
            atasozu.setIcerik(cursor.getString(1));
            atasozleri.add(atasozu);
        }

        return atasozleri;
    }

    public List<Deyim>DeyimArama(String aranan){
        List<Deyim> deyimler = new ArrayList<Deyim>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlQuery = "SELECT  * FROM " + TABLE_DEYIMLER +" WHERE icerik LIKE '"+aranan+"%' OR icerik LIKE '% "+aranan+"%'";
        //String sqlQuery = "SELECT  * FROM " + TABLE_ATASOZLERI +" WHERE CONTAINS(icerik,'"+aranan+"')";
        Cursor cursor = db.rawQuery(sqlQuery, null);

        while (cursor.moveToNext()) {
            Deyim deyim = new Deyim();
            deyim.setId(cursor.getInt(0));
            deyim.setIcerik(cursor.getString(1));
            deyimler.add(deyim);
        }

        return deyimler;
    }
}