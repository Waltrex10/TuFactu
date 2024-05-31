package com.pluartz.tufactu.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pluartz.tufactu.entidades.LFactura;

import java.util.ArrayList;

//BASE DE DATOS FACTURA
public class DBFactura extends DBHelper {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    public DBFactura(Context context) {
        super(context);
        DBFactura.context = context;
    }

    //INSERTAR FACTURA
    public long insertarFactura(String numero, String fecha, String descripcion, String dnicliente, String dniusuario){

        long id = 0;

        try (DBHelper dbhelper = new DBHelper(context)){
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("numero", numero);
            values.put("fecha", fecha);
            values.put("descripcion", descripcion);
            values.put("dnicliente", dnicliente);
            values.put("dniusuario", dniusuario);

            id = db.insert(TABLE_FACTURAS, null, values);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return id;
    }

    //MOSTRAR FACTURAS
    public static ArrayList<LFactura> mostrarFactura() {

        ArrayList<LFactura> listaFactura = new ArrayList<>();
        LFactura factura;
        Cursor cursorFactura = null;

        try (DBHelper dbhelper = new DBHelper(context)){
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            cursorFactura = db.rawQuery("SELECT * FROM " + TABLE_FACTURAS, null);

            if (cursorFactura.moveToFirst()) {
                do {
                    factura = new LFactura();
                    factura.setId(cursorFactura.getInt(0));
                    factura.setNumero(cursorFactura.getString(1));
                    factura.setFecha(cursorFactura.getString(2));
                    factura.setDescripcion(cursorFactura.getString(3));
                    factura.setDnicliente(cursorFactura.getString(4));
                    factura.setDniusuario(cursorFactura.getString(5));
                    listaFactura.add(factura);
                } while (cursorFactura.moveToNext());
            } else {
                Log.d("DBFactura", "No se encontró factura en la base de datos..");
            }
        } catch (Exception ex) {
            Log.e("DBFactura", "Error al recuperar la factura", ex);
        } finally {
            if (cursorFactura != null) {
                cursorFactura.close();
            }
        }
        return listaFactura;
    }

    //VER FACTURA
    public LFactura verFactura(int id) {

        LFactura factura = null;
        Cursor cursorFactura;
        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        cursorFactura = db.rawQuery("SELECT * FROM " + TABLE_FACTURAS + " WHERE id = " + id + " LIMIT 1 ", null);
        if (cursorFactura.moveToFirst()) {
            factura = new LFactura();
            factura.setId(cursorFactura.getInt(0));
            factura.setNumero(cursorFactura.getString(1));
            factura.setFecha(cursorFactura.getString(2));
            factura.setDescripcion(cursorFactura.getString(3));
            factura.setDnicliente(cursorFactura.getString(4));
            factura.setDniusuario(cursorFactura.getString(5));
        }
        cursorFactura.close();
        return factura;
    }

    //EDITAR FACTURA
    public boolean editarFactura(int id, String numero, String fecha, String descripcion, String dnicliente){

        boolean correcto = false;
        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {
            db.execSQL("UPDATE " + TABLE_FACTURAS + " SET numero = '"+numero+"', fecha = '"+fecha+"', descripcion = '"+descripcion+"', dnicliente = '"+dnicliente+"' WHERE id='" + id + "'");
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

    //ELIMINAR FACTURA
    public boolean eliminarFactura(int id){
        boolean correcto = false;
        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + TABLE_FACTURAS + " WHERE id = '" + id + "'");
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
