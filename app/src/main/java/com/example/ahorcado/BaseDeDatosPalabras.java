package com.example.ahorcado;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDeDatosPalabras extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Palabras";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "TablaPalabras";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PALABRA = "palabra";
    public static final String COLUMN_DEFINICION = "definicion";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PALABRA + " TEXT, "
            + COLUMN_DEFINICION + " TEXT);";

    public BaseDeDatosPalabras(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}