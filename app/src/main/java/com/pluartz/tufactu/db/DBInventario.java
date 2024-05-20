package com.pluartz.tufactu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBInventario extends DBHelper {
    Context context;

    public DBInventario(Context context) {
        super(context);
        this.context = context;
    }

    public long insertaProducto_Servicio(String nombre, String precio){

        long id = 0;

        try {
            DBHelper dbhelper = new DBHelper(context);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("apellidos", precio);

            id = db.insert(TABLE_INVENTARIO, null, values);
        } catch (Exception ex){
            ex.toString();
        }

        return id;

    }
}
