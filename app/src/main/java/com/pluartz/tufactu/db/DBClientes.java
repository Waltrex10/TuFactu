package com.pluartz.tufactu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pluartz.tufactu.entidades.LClientes;

import java.util.ArrayList;

//Metodos de la base de datos clientes
public class DBClientes extends DBHelper {
    static Context context;

    public DBClientes(Context context) {
        super(context);
        DBClientes.context = context;
    }

    //Metodo para insertar un cliente
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

    //Metodo para motrar todos los clientes
    public static ArrayList<LClientes> mostrarClientes() {
        ArrayList<LClientes> listaClientes = new ArrayList<>();
        LClientes cliente = null;
        Cursor cursorClientes = null;

        try {
            DBHelper dbhelper = new DBHelper(context);
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            cursorClientes = db.rawQuery("SELECT * FROM " + TABLE_CLIENTES, null);

            if (cursorClientes.moveToFirst()) {
                do {
                    cliente = new LClientes();
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

    //Metodo para ver un cliente
    public LClientes verCliente(int id) {

        LClientes cliente = null;
        Cursor cursorClientes;

        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        cursorClientes = db.rawQuery("SELECT * FROM " + TABLE_CLIENTES + " WHERE id = " + id + " LIMIT 1 ", null);

        if (cursorClientes.moveToFirst()) {
            cliente = new LClientes();
            cliente.setId(cursorClientes.getInt(0));
            cliente.setNombre(cursorClientes.getString(1));
            cliente.setApellidos(cursorClientes.getString(2));
            cliente.setDni(cursorClientes.getString(3));
            cliente.setCorreo(cursorClientes.getString(4));
            cliente.setDireccion(cursorClientes.getString(5));
            cliente.setTelefono(cursorClientes.getString(6));
            cliente.setDniusuario(cursorClientes.getString(7));

        }
        cursorClientes.close();
        return cliente;
    }

    //Metodo para editar un cliente
    public boolean editarCliente(int id, String nombre, String apellidos, String dni, String correo,  String direccion, String telefono){

        boolean correcto = false;

        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + TABLE_CLIENTES + " SET nombre = '"+nombre+"', apellidos = '"+apellidos+"', dni = '"+dni+"', correo = '"+correo+"', direccion = '"+direccion+"', telefono = '"+telefono+"' WHERE id='" + id + "'");
            correcto = true;
        } catch (Exception ex){
            correcto = false;
            ex.toString();
        } finally {
            db.close();
        }
        return correcto;

    }

    //Metodo para eliminar un cliente
    public boolean eliminarCliente(int id){

        boolean correcto = false;

        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM " + TABLE_CLIENTES + " WHERE id = '" + id + "'");
            correcto = true;
        } catch (Exception ex){
            correcto = false;
            ex.toString();
        } finally {
            db.close();
        }
        return correcto;

    }

}
