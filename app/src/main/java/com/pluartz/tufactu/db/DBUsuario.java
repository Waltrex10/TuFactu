package com.pluartz.tufactu.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pluartz.tufactu.entidades.LUsuario;

//BASE DE DATOS USUARIO
public class DBUsuario extends DBHelper {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    public DBUsuario(Context context) {
        super(context);
        DBUsuario.context = context;
    }

    //INSERTAR USUARIO
    public long insertaUsuario(String nombre, String apellidos, String dni, String correo, String contrasena, String empresa, String telefono, String postal, String direccion, byte[] logo){
        long id = 0;
        try (DBHelper dbhelper = new DBHelper(context)){
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
            ex.printStackTrace();
        }
        return id;
    }
    //PARA VER SI EXISTE YA UN USUARIO CON ESE DNI
    public boolean existeUsuarioConDNI(String dni) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USUARIOS + " WHERE dni = ?", new String[]{dni});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existe;
    }
    //VERIFICAR PARA INICIO SESION
    public boolean verificarCredenciales(String dni, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USUARIOS + " WHERE dni = ? AND contrasena = ?", new String[]{dni, contrasena});
        boolean credencialesValidas = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return credencialesValidas;
    }
    //VER USUARIO
    public LUsuario verUsuario(String dni) {
        LUsuario usuario = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorUsuario = db.rawQuery("SELECT * FROM usuarios WHERE dni = ?", new String[]{dni});
        if (cursorUsuario.moveToFirst()) {
            usuario = new LUsuario();
            usuario.setId(cursorUsuario.getInt(0));
            usuario.setNombre(cursorUsuario.getString(1));
            usuario.setApellidos(cursorUsuario.getString(2));
            usuario.setDni(cursorUsuario.getString(3));
            usuario.setCorreo(cursorUsuario.getString(4));
            usuario.setContrasena(cursorUsuario.getString(5));
            usuario.setEmpresa(cursorUsuario.getString(6));
            usuario.setTelefono(cursorUsuario.getString(7));
            usuario.setPostal(cursorUsuario.getString(8));
            usuario.setDireccion(cursorUsuario.getString(9));
        }
        cursorUsuario.close();
        return usuario;
    }

    //EDITAR USUARIO
    public boolean editarUsuario(String dni, String nombre, String apellidos, String correo, String contrasena, String empresa, String telefono, String postal, String direccion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("apellidos", apellidos);
        values.put("correo", correo);
        values.put("contrasena", contrasena);
        values.put("empresa", empresa);
        values.put("telefono", telefono);
        values.put("postal", postal);
        values.put("direccion", direccion);

        int rows = db.update("usuarios", values, "dni = ?", new String[]{dni});
        return rows > 0;
    }

}
