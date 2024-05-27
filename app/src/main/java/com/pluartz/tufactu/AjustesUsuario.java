package com.pluartz.tufactu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pluartz.tufactu.db.DBUsuario;
import com.pluartz.tufactu.entidades.LUsuario;

public class AjustesUsuario extends AppCompatActivity {

    private EditText et_nombre, et_apellidos, et_correo, et_contrasena, et_concontrasena, et_nombreempresa, et_telefono, et_postal, et_direccion;
    boolean correcto = false;
    LUsuario usuario;
    String dni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_usuario);

        et_nombre = findViewById(R.id.et_nombrea);
        et_apellidos = findViewById(R.id.et_apellidosa);
        et_correo = findViewById(R.id.et_correoa);
        et_contrasena = findViewById(R.id.et_contrasenaa);
        et_concontrasena = findViewById(R.id.et_concontrasenaa);
        et_nombreempresa = findViewById(R.id.et_nombreempresaa);
        et_telefono = findViewById(R.id.et_telefonoa);
        et_postal = findViewById(R.id.et_postala);
        et_direccion = findViewById(R.id.et_direcciona);

        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        dni = sharedPref.getString("dniusuario", null);

        if (dni == null) {
            Toast.makeText(this, "DNI de usuario no válido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        DBUsuario dbUsuario = new DBUsuario(AjustesUsuario.this);
        usuario = dbUsuario.verUsuario(dni);

        if (usuario != null) {
            et_nombre.setText(usuario.getNombre());
            et_apellidos.setText(usuario.getApellidos());
            et_correo.setText(usuario.getCorreo());
            et_contrasena.setText(usuario.getContrasena());
            et_nombreempresa.setText(usuario.getEmpresa());
            et_telefono.setText(usuario.getTelefono());
            et_postal.setText(usuario.getPostal());
            et_direccion.setText(usuario.getDireccion());
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_LONG).show();
            finish();
        }

        Button but_guardar = findViewById(R.id.but_guardara);
        but_guardar.setOnClickListener(v -> {
            String contrasena = et_contrasena.getText().toString();
            String concontrasena = et_concontrasena.getText().toString();
            if (!contrasenaCorrecta(contrasena)){
                Toast.makeText(AjustesUsuario.this, "La contraseña tiene que tener al menos 3 caracteres incluyendo letras mayúsculas, minúsculas y números", Toast.LENGTH_SHORT).show();
            } else if (!contrasena.equals(concontrasena)){
                Toast.makeText(AjustesUsuario.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            } else {
                if (!et_nombre.getText().toString().equals("") && !et_apellidos.getText().toString().equals("")) {
                    correcto = dbUsuario.editarUsuario(dni, et_nombre.getText().toString(), et_apellidos.getText().toString(), et_correo.getText().toString(), et_contrasena.getText().toString(), et_nombreempresa.getText().toString(), et_telefono.getText().toString(), et_postal.getText().toString(), et_direccion.getText().toString());
                    if (correcto) {
                        Toast.makeText(AjustesUsuario.this, "Registro modificado", Toast.LENGTH_LONG).show();
                        limpiar();
                        Volver();
                    } else {
                        Toast.makeText(AjustesUsuario.this, "Error al modificar cliente", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AjustesUsuario.this, "Debe llenar los campos", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //VOLVER
    private void Volver(){
        Intent intent = new Intent(this, Ajustes.class);
        intent.putExtra("dni", dni);
        startActivity(intent);
    }

    //VALIDAR CONTRASEÑA
    private boolean contrasenaCorrecta(String contrasena) {
        return contrasena.length() > 3 && contrasena.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    }

    //LIMPIAR
    private void limpiar(){
        et_nombre.setText("");
        et_apellidos.setText("");
        et_correo.setText("");
        et_contrasena.setText("");
        et_concontrasena.setText("");
        et_nombreempresa.setText("");
        et_telefono.setText("");
        et_postal.setText("");
        et_direccion.setText("");
    }

}