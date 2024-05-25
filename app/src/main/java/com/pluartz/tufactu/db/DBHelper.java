package com.pluartz.tufactu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//CREACION DE TABLAS DBHELPER
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE = "lista.db";
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String TABLE_CLIENTES = "clientes";
    public static final String TABLE_INVENTARIO = "inventario";
    public static final String TABLE_FACTURAS = "facturas";
    public static final String TABLE_PRESUPUESTO = "presupuestos";
    public DBHelper(Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_USUARIOS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "apellidos TEXT NOT NULL," +
                "dni TEXT NOT NULL," +
                "correo TEXT NOT NULL," +
                "contrasena TEXT NOT NULL," +
                "empresa TEXT NOT NULL," +
                "telefono TEXT NOT NULL," +
                "postal TEXT NOT NULL," +
                "direccion TEXT NOT NULL," +
                "logo BLOB)");

        db.execSQL("CREATE TABLE " + TABLE_CLIENTES + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "Apellidos TEXT NOT NULL," +
                "dni TEXT NOT NULL," +
                "correo TEXT NOT NULL," +
                "telefono TEXT NOT NULL," +
                "direccion TEXT NOT NULL," +
                "dniusuario TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_INVENTARIO + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "precio TEXT NOT NULL," +
                "dniusuario TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_FACTURAS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "numero TEXT NOT NULL," +
                "fecha TEXT NOT NULL," +
                "descripcion TEXT NOT NULL," +
                "dniusuario TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_PRESUPUESTO + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "numero TEXT NOT NULL," +
                "fecha TEXT NOT NULL," +
                "descripcion TEXT NOT NULL," +
                "dniusuario TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
