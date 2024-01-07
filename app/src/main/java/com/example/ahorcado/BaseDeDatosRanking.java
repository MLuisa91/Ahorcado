package com.example.ahorcado;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDatosRanking extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Ranking";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "TablaPuntuacion";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JUGADOR = "jugador";
    public static final String COLUMN_PUNTUACION = "puntuacion";
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_JUGADOR + " TEXT, "
                    + COLUMN_PUNTUACION + " INTEGER);";

    public BaseDeDatosRanking(Context context) {
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);
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
