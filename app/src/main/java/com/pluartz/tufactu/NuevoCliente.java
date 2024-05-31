package com.pluartz.tufactu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pluartz.tufactu.db.DBClientes;

//CREAR CLIENTE
public class NuevoCliente extends AppCompatActivity {

    EditText et_nombre, et_apellidos, et_dni, et_correo, et_telefono, et_direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_cliente);

        et_nombre = findViewById(R.id.et_nombrenc);
        et_apellidos = findViewById(R.id.et_apellidosnc);
        et_dni = findViewById(R.id.et_dninc);
        et_correo = findViewById(R.id.et_correonc);
        et_telefono = findViewById(R.id.et_telefononc);
        et_direccion = findViewById(R.id.et_direccionnc);
        Button but_guardar = findViewById(R.id.but_guardarnc);

        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        final String dni = sharedPref.getString("dniusuario", String.valueOf(-1));

        //REGISTRAR CLIENTE
        but_guardar.setOnClickListener(v -> {
            try (DBClientes DBClientes = new DBClientes(NuevoCliente.this)){
                long id = DBClientes.insertaCliente(et_nombre.getText().toString(),et_apellidos.getText().toString(),et_dni.getText().toString(),et_correo.getText().toString(),et_telefono.getText().toString(), et_direccion.getText().toString(), dni);
                if (id > 0){
                    String guardadoc = getString(R.string.guardadoc);
                    Toast.makeText(NuevoCliente.this, guardadoc, Toast.LENGTH_LONG).show();
                    limpiar();
                    Intent Clientes = new Intent(NuevoCliente.this, Clientes.class);
                    startActivity(Clientes);
                    finish();
                } else {
                    String errorc = getString(R.string.errorc);
                    Toast.makeText(NuevoCliente.this, errorc, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e){
                Toast.makeText(NuevoCliente.this, "Ocurrió un error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void limpiar(){
        et_nombre.setText("");
        et_apellidos.setText("");
        et_dni.setText("");
        et_correo.setText("");
        et_direccion.setText("");
        et_telefono.setText("");
    }

}