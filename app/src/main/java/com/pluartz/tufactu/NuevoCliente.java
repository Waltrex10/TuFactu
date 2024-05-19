package com.pluartz.tufactu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pluartz.tufactu.db.DBClientes;

public class NuevoCliente extends AppCompatActivity {

    private EditText et_nombre, et_apellidos, et_dni, et_correo, et_direccion, et_telefono;
    private Button but_guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevocliente);

        et_nombre = (EditText) findViewById(R.id.et_nombrec);
        et_apellidos = (EditText) findViewById(R.id.et_apellidosc);
        et_dni = (EditText) findViewById(R.id.et_dnic);
        et_correo = (EditText) findViewById(R.id.et_correoc);
        et_direccion = (EditText) findViewById(R.id.et_direccionc);
        et_telefono = (EditText) findViewById(R.id.et_telefonoc);
        but_guardar = (Button) findViewById(R.id.but_guardarc);

        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        final String dni = sharedPref.getString("dniusuario", String.valueOf(-1));

        but_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBClientes DBClientes = new DBClientes(NuevoCliente.this);
                long id = DBClientes.insertaCliente(et_nombre.getText().toString(),et_apellidos.getText().toString(),et_dni.getText().toString(),et_correo.getText().toString(),et_direccion.getText().toString(),et_telefono.getText().toString(), dni);

                if (id > 0){
                    Toast.makeText(NuevoCliente.this, "Cliente guardado", Toast.LENGTH_LONG).show();
                    limpiar();
                    Intent Clientes = new Intent(NuevoCliente.this, Clientes.class);
                    startActivity(Clientes);
                    finish();
                } else {
                    Toast.makeText(NuevoCliente.this, "Error al guardar", Toast.LENGTH_LONG).show();
                }

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