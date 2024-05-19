package com.pluartz.tufactu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pluartz.tufactu.entidades.lClientes;

import java.util.ArrayList;

public class DBClientes extends DBHelper {
    static Context context;

    public DBClientes(Context context) {
        super(context);
        DBClientes.context = context;
    }

    public long insertaCliente(String nombre, String apellidos, String dni, String correo,  String direccion, String telefono, String dniusuario){

        long id = 0;

        try {
            DBHelper dbhelper = new DBHelper(context);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("apellidos", apellidos);
            values.put("dni", dni);
            values.put("correo", correo);
            values.put("direccion", direccion);
            values.put("telefono", telefono);
            values.put("dniusuario", dniusuario);

            id = db.insert(TABLE_CLIENTES, null, values);
        } catch (Exception ex){
            ex.toString();
        }
        return id;

    }

    public static ArrayList<lClientes> mostrarClientes() {
        ArrayList<lClientes> listaClientes = new ArrayList<>();
        lClientes cliente = null;
        Cursor cursorClientes = null;

        try {
            DBHelper dbhelper = new DBHelper(context);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            cursorClientes = db.rawQuery("SELECT * FROM " + TABLE_CLIENTES, null);

            if (cursorClientes.moveToFirst()) {
                do {
                    cliente = new lClientes();
                    cliente.setId(cursorClientes.getInt(0));
                    cliente.setNombre(cursorClientes.getString(1));
                    cliente.setApellidos(cursorClientes.getString(2));
                    cliente.setDni(cursorClientes.getString(3));
                    cliente.setCorreo(cursorClientes.getString(4));
                    cliente.setDireccion(cursorClientes.getString(5));
                    cliente.setTelefono(cursorClientes.getString(6));
                    cliente.setDniusuario(cursorClientes.getString(7));
                    listaClientes.add(cliente);
                } while (cursorClientes.moveToNext());
            } else {
                Log.d("DBClientes", "No clients found in the database.");
            }
        } catch (Exception ex) {
            Log.e("DBClientes", "Error fetching clients", ex);
        } finally {
            if (cursorClientes != null) {
                cursorClientes.close();
            }
        }

        return listaClientes;
    }

}
