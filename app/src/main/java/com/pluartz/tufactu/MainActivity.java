package com.pluartz.tufactu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.pluartz.tufactu.db.DBUsuario;

//INICIO SESION Y DE LA APLICACION
public class MainActivity extends AppCompatActivity {

    private EditText et_dni, et_contrasena;
    private CheckBox check_recordar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_dni = findViewById(R.id.et_dni);
        et_contrasena = findViewById(R.id.et_contrasena);
        check_recordar = findViewById(R.id.check_recordar);
        Button but_sesion = findViewById(R.id.but_iniciar_sesion);
        Button but_registro = findViewById(R.id.but_registro);

        sharedPreferences = getSharedPreferences("datos_login", Context.MODE_PRIVATE);

        boolean recordar = sharedPreferences.getBoolean("recordar", false);
        if (recordar) {
            et_dni.setText(sharedPreferences.getString("dni", ""));
            et_contrasena.setText(sharedPreferences.getString("contrasena", ""));
            check_recordar.setChecked(true);
        }

        but_sesion.setOnClickListener(v -> {

            String dni = et_dni.getText().toString().trim().toUpperCase();
            String contrasena = et_contrasena.getText().toString().trim();
            iniciarSesion(dni, contrasena);

            if (check_recordar.isChecked()) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("dni", dni);
                editor.putString("contrasena", contrasena);
                editor.putBoolean("recordar", true);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("dni");
                editor.remove("contrasena");
                editor.putBoolean("recordar", false);
                editor.apply();
            }

        });

        but_registro.setOnClickListener(v -> {
            Intent registro = new Intent(MainActivity.this, Registro.class);
            startActivity(registro);
        });

    }

    //INICIAR SESION
    private void iniciarSesion(String dni, String contrasena) {
        try (DBUsuario dbUsuario = new DBUsuario(this)) {
            if (dbUsuario.verificarCredenciales(dni, contrasena)) {

                SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("dniusuario", dni);
                editor.apply();
                Intent facturas = new Intent(MainActivity.this, Facturas.class);
                startActivity(facturas);
                finish();
            } else {
                String mensage = getString(R.string.incorrecto);
                Toast.makeText(MainActivity.this, mensage, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
        }
    }
}