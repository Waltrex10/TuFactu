package com.pluartz.tufactu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pluartz.tufactu.entidades.LClientes;

import java.util.ArrayList;

public class DBClientes extends DBHelper {
    static Context context;

    public DBClientes(Context context) {
        super(context);
        DBClientes.context = context;
    }

    public long insertaCliente(String nombre, String apellidos, String dni, String correo,  String direccion, String telefono){

        long id = 0;

        try {
            DBHelper dbhelper = new DBHelper(context);
            SQLiteDatabase db = dbhelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("apellidos", apellidos);
            values.put("dni", dni);
            values.put("correo", correo);
            values.put("telefono", telefono);
            values.put("direccion", direccion);

            id = db.insert(TABLE_CLIENTES, null, values);
        } catch (Exception ex){
            ex.toString();
        }

        return id;

    }

    public static ArrayList<LClientes> mostrarClientes(){
        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        ArrayList<LClientes> listaClientes = new ArrayList<>();
        LClientes cliente = null;
        Cursor cursorClientes = null;

        cursorClientes = db.rawQuery("SELECT * FROM " + TABLE_CLIENTES, null);

        if(cursorClientes.moveToFirst()){
            do {
                cliente = new LClientes();
                cliente.setId(cursorClientes.getInt(0));
                cliente.setNombre(cursorClientes.getString(1));
                cliente.setApellidos(cursorClientes.getString(2));
                cliente.setDni(cursorClientes.getString(3));
                cliente.setCorreo(cursorClientes.getString(4));
                cliente.setTelefono(cursorClientes.getString(5));
                cliente.setDireccion(cursorClientes.getString(6));
                listaClientes.add(cliente);
            } while (cursorClientes.moveToNext());
        }
        cursorClientes.close();

        return listaClientes;

    }

}
