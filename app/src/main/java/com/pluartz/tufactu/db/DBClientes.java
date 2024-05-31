package com.pluartz.tufactu.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pluartz.tufactu.entidades.LClientes;

import java.util.ArrayList;

//BASE DE DATOS CLIENTES
public class DBClientes extends DBHelper {
    @SuppressLint("StaticFieldLeak")
    static Context context;

    public DBClientes(Context context) {
        super(context);
        DBClientes.context = context;
    }

    //INSERTAR CLIENTES
    public long insertaCliente(String nombre, String apellidos, String dni, String correo, String telefono, String direccion, String dniusuario){
        long id = 0;
        try (DBHelper dbhelper = new DBHelper(context)){
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("apellidos", apellidos);
            values.put("dni", dni);
            values.put("correo", correo);
            values.put("telefono", telefono);
            values.put("direccion", direccion);
            values.put("dniusuario", dniusuario);
            id = db.insert(TABLE_CLIENTES, null, values);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return id;
    }
    //MOSTRAR CLIENTES
    public static ArrayList<LClientes> mostrarClientes() {
        ArrayList<LClientes> listaClientes = new ArrayList<>();
        LClientes cliente;
        Cursor cursorClientes = null;
        try (DBHelper dbhelper = new DBHelper(context)){
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
                    cliente.setTelefono(cursorClientes.getString(5));
                    cliente.setDireccion(cursorClientes.getString(6));
                    cliente.setDniusuario(cursorClientes.getString(7));
                    listaClientes.add(cliente);
                } while (cursorClientes.moveToNext());
            } else {
                Log.d("DBClientes", "No se encontraron clientes en la base de datos.");
            }
        } catch (Exception ex) {
            Log.e("DBClientes", "Error al recuperar clientes", ex);
        } finally {
            if (cursorClientes != null) {
                cursorClientes.close();
            }
        }
        return listaClientes;
    }
    //VER CLEINTE
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
            cliente.setTelefono(cursorClientes.getString(5));
            cliente.setDireccion(cursorClientes.getString(6));
            cliente.setDniusuario(cursorClientes.getString(7));
        }
        cursorClientes.close();
        return cliente;
    }
    //EDITAR CLIENTE
    public boolean editarCliente(int id, String nombre, String apellidos, String dni, String correo, String telefono,  String direccion){
        boolean correcto = false;
        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {
            db.execSQL("UPDATE " + TABLE_CLIENTES + " SET nombre = '"+nombre+"', apellidos = '"+apellidos+"', dni = '"+dni+"', correo = '"+correo+"', direccion = '"+direccion+"', telefono = '"+telefono+"' WHERE id='" + id + "'");
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
    //ELIMINAR CLIENTE
    public boolean eliminarCliente(int id){
        boolean correcto = false;
        DBHelper dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + TABLE_CLIENTES + " WHERE id = '" + id + "'");
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

    //LISTA DNI
    public ArrayList<String> obtenerDniClientes() {
        ArrayList<String> listaDni = new ArrayList<>();
        Cursor cursorClientes = null;
        try (DBHelper dbhelper = new DBHelper(context)){
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            cursorClientes = db.rawQuery("SELECT dni FROM " + TABLE_CLIENTES, null);
            if (cursorClientes.moveToFirst()) {
                do {
                    listaDni.add(cursorClientes.getString(0));
                } while (cursorClientes.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursorClientes != null) {
                cursorClientes.close();
            }
        }
        return listaDni;
    }

    //OBTENER LOS CLIENTES MEDIANTE EL DNI
    public LClientes obtenerClientePorDNI(String dni) {
        LClientes cliente = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CLIENTES + " WHERE dni = ?", new String[]{dni});
        if (cursor.moveToFirst()) {
            cliente = new LClientes();
            cliente.setId(cursor.getInt(0));
            cliente.setNombre(cursor.getString(1));
            cliente.setApellidos(cursor.getString(2));
            cliente.setDni(cursor.getString(3));
            cliente.setCorreo(cursor.getString(4));
            cliente.setTelefono(cursor.getString(5));
            cliente.setDireccion(cursor.getString(6));
            cliente.setDniusuario(cursor.getString(7));
        }
        cursor.close();
        return cliente;
    }

}
