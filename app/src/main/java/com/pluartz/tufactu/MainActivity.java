package com.pluartz.tufactu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.pluartz.tufactu.db.DBUsuario;

public class MainActivity extends AppCompatActivity {

    private EditText et_dni, et_contrasena;
    private CheckBox check_recordar;
    private Button but_sesion, but_registro;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_dni = (EditText) findViewById(R.id.et_dni);
        et_contrasena = (EditText) findViewById(R.id.et_contrasena);
        check_recordar = (CheckBox)findViewById(R.id.check_recordar);
        but_sesion = (Button) findViewById(R.id.but_iniciar_sesion);
        but_registro = (Button) findViewById(R.id.but_registro);

        sharedPreferences = getSharedPreferences("datos_login", Context.MODE_PRIVATE);

        boolean recordar = sharedPreferences.getBoolean("recordar", false);
        if (recordar) {
            et_dni.setText(sharedPreferences.getString("dni", ""));
            et_contrasena.setText(sharedPreferences.getString("contrasena", ""));
            check_recordar.setChecked(true);
        }

        but_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dni = et_dni.getText().toString().trim();
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

            }
        });

        but_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registro = new Intent(MainActivity.this, Registro.class);
                startActivity(registro);
            }
        });

    }

    private void iniciarSesion(String dni, String contrasena) {
        DBUsuario dbUsuario = new DBUsuario(this);

        if (dbUsuario.verificarCredenciales(dni, contrasena)) {
            Intent facturas = new Intent(MainActivity.this, Facturas.class);
            startActivity(facturas);
            finish();
        } else {
            Toast.makeText(MainActivity.this, "DNI o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }

    }

}