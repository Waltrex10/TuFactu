package com.pluartz.tufactu.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pluartz.tufactu.entidades.LPresupuesto;

import java.util.ArrayList;

//BASE DE DATOS PRESUPUESTO
public class DBPresupuesto extends DBHelper {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    public DBPresupuesto(Context context) {
        super(context);
        DBPresupuesto.context = context;
    }

    //INSERTAR PRESUPUESTO
    public long insertarPresupuesto(String numero, String fecha, String descripcion, String dnicliente, String dniusuario){
        long id = 0;
        try (DBHelper dbhelper = new DBHelper(context)){
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("numero", numero);
            values.put("fecha", fecha);
            values.put("descripcion", descripcion);
            values.put("dnicliente", dnicliente);
            values.put("dniusuario", dniusuario);

            id = db.insert(TABLE_PRESUPUESTO, null, values);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return id;
    }
    //MOSTRAR PRESUPUESTO
    public static ArrayList<LPresupuesto> mostrarPresupuesto() {
        ArrayList<LPresupuesto> listaPresupuesto = new ArrayList<>();
        LPresupuesto presupuesto;
        Cursor cursorPresupuesto = null;
        try (DBHelper dbhelper = new DBHelper(context)){
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            cursorPresupuesto = db.rawQuery("SELECT * FROM " + TABLE_PRESUPUESTO, null);
            if (cursorPresupuesto.moveToFirst()) {
                do {
                    presupuesto = new LPresupuesto();
                    presupuesto.setId(cursorPresupuesto.getInt(0));
                    presupuesto.setNumero(cursorPresupuesto.getString(1));
                    presupuesto.setFecha(cursorPresupuesto.getString(2));
                    presupuesto.setDescripcion(cursorPresupuesto.getString(3));
                    presupuesto.setDnicliente(cursorPresupuesto.getString(4));
                    presupuesto.setDniusuario(cursorPresupuesto.getString(5));
                    listaPresupuesto.add(presupuesto);
                } while (cursorPresupuesto.moveToNext());
            } else {
                Log.d("DBPresupuesto", "No se encontró presupuesto en la base de datos..");
            }
        } catch (Exception ex) {
            Log.e("DBPresupuesto", "Error al recuperar el presupuesto", ex);
        } finally {
            if (cursorPresupuesto != null) {
                cursorPresupuesto.close();
            }
        }
        return listaPresupuesto;
    }
    //VER PRESPUESTO
    public LPresupuesto verPresupuesto(int id) {
        LPresupuesto presupuesto = null;
        Cursor cursorPresupuesto;
        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        cursorPresupuesto = db.rawQuery("SELECT * FROM " + TABLE_PRESUPUESTO + " WHERE id = " + id + " LIMIT 1 ", null);
        if (cursorPresupuesto.moveToFirst()) {
            presupuesto = new LPresupuesto();
            presupuesto.setId(cursorPresupuesto.getInt(0));
            presupuesto.setNumero(cursorPresupuesto.getString(1));
            presupuesto.setFecha(cursorPresupuesto.getString(2));
            presupuesto.setDescripcion(cursorPresupuesto.getString(3));
            presupuesto.setDnicliente(cursorPresupuesto.getString(4));
            presupuesto.setDniusuario(cursorPresupuesto.getString(5));
        }
        cursorPresupuesto.close();
        return presupuesto;
    }

    //EDITAR PRESUPUESTO
    public boolean editarPresuspuesto(int id, String numero, String fecha, String descripcion, String dnicliente){
        boolean correcto = false;
        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {
            db.execSQL("UPDATE " + TABLE_PRESUPUESTO + " SET numero = '"+numero+"', fecha = '"+fecha+"', descripcion = '"+descripcion+"', dnicliente = '"+dnicliente+"'  WHERE id='" + id + "'");
            correcto = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return correcto;
    }

    //ELIMINAR PRESUPUESTO
    public boolean eliminarPresupuesto(int id){
        boolean correcto = false;
        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + TABLE_PRESUPUESTO+ " WHERE id = '" + id + "'");
            correcto = true;
        }  catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return correcto;
    }

}
