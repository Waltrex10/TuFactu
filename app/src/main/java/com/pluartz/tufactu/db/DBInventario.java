package com.pluartz.tufactu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pluartz.tufactu.entidades.LInventario;

import java.util.ArrayList;

//Metodos de la base de datos inventario
public class DBInventario extends DBHelper {
    static Context context;

    public DBInventario(Context context) {
        super(context);
        DBInventario.context = context;
    }

    //Metodo insertar inventario
    public long insertaInventario(String nombre, String precio, String dniusuario) {

        long id = 0;

        try (DBHelper dbhelper = new DBHelper(context)){
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("precio", precio);
            values.put("dniusuario", dniusuario);

            id = db.insert(TABLE_INVENTARIO, null, values);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;

    }

    //Metodo pra mostrar el inventario
    public static ArrayList<LInventario> mostrarInventario() {
        ArrayList<LInventario> listaInventario = new ArrayList<>();
        LInventario inventario = null;
        Cursor cursorInventario = null;

        try (DBHelper dbhelper = new DBHelper(context)){
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            cursorInventario = db.rawQuery("SELECT * FROM " + TABLE_INVENTARIO, null);

            if (cursorInventario.moveToFirst()) {
                do {
                    inventario = new LInventario();
                    inventario.setId(cursorInventario.getInt(0));
                    inventario.setNombre(cursorInventario.getString(1));
                    inventario.setPrecio(cursorInventario.getString(2));
                    inventario.setDniusuario(cursorInventario.getString(3));
                    listaInventario.add(inventario);
                } while (cursorInventario.moveToNext());
            } else {
                Log.d("DBInventario", "No se encontró inventario en la base de datos..");
            }
        } catch (Exception ex) {
            Log.e("DBInventario", "Error al recuperar el inventario", ex);
        } finally {
            if (cursorInventario != null) {
                cursorInventario.close();
            }
        }

        return listaInventario;
    }

    //Metodo para ver un inventario
    public LInventario verInventario(int id) {

        LInventario inventario = null;
        Cursor cursorInventario;

        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        cursorInventario = db.rawQuery("SELECT * FROM " + TABLE_INVENTARIO + " WHERE id = " + id + " LIMIT 1 ", null);

        if (cursorInventario.moveToFirst()) {
            inventario = new LInventario();
            inventario.setId(cursorInventario.getInt(0));
            inventario.setNombre(cursorInventario.getString(1));
            inventario.setPrecio(cursorInventario.getString(2));
            inventario.setDniusuario(cursorInventario.getString(3));

        }
        cursorInventario.close();
        return inventario;
    }

    //Metodo para editar un inventario
    public boolean editarInventario(int id, String nombre, String precio){

        boolean correcto = false;

        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + TABLE_INVENTARIO + " SET nombre = '"+nombre+"', precio = '"+precio+"' WHERE id='" + id + "'");
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

    //Metodo para eliminar un inventario
    public boolean eliminarInventario(int id){

        boolean correcto = false;

        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM " + TABLE_INVENTARIO + " WHERE id = '" + id + "'");
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
