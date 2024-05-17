package com.pluartz.tufactu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUsuario extends DBHelper {

    Context context;

    public DBUsuario(Context context) {
        super(context);
        this.context = context;
    }

    public long insertaUsuario(String nombre, String apellidos, String dni, String correo, String contrasena, String empresa, String telefono, String postal, String direccion, byte[] logo){

        long id = 0;

        try {
            DBHelper dbhelper = new DBHelper(context);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("apellidos", apellidos);
            values.put("dni", dni);
            values.put("correo", correo);
            values.put("contrasena", contrasena);
            values.put("empresa", empresa);
            values.put("telefono", telefono);
            values.put("postal", postal);
            values.put("direccion", direccion);
            values.put("logo", logo);

            id = db.insert(TABLE_USUARIOS, null, values);
        } catch (Exception ex){
            ex.toString();
        }

        return id;

    }
    public boolean existeUsuarioConDNI(String dni) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USUARIOS + " WHERE dni = ?", new String[]{dni});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existe;
    }

    public boolean verificarCredenciales(String dni, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USUARIOS + " WHERE dni = ? AND contrasena = ?", new String[]{dni, contrasena});
        boolean credencialesValidas = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return credencialesValidas;
    }

}
