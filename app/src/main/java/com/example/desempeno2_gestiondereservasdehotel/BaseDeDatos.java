package com.example.desempeno2_gestiondereservasdehotel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDatos extends SQLiteOpenHelper {
    private static final String NOMBRE_BD = "hotel.db";
    private static final int VERSION_BD = 1;

    public BaseDeDatos(Context context, String reservas, Object o, int i) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE reservas (" +
        "nombre TEXT, dni TEXT, email TEXT, " +
        "tipo_habitacion TEXT, noches INTEGER, total REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS reservas");
        onCreate(db);
    }

}
