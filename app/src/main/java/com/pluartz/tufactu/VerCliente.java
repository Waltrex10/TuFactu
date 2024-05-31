package com.pluartz.tufactu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pluartz.tufactu.db.DBClientes;
import com.pluartz.tufactu.entidades.LClientes;

//VER, EDITAR Y ELIMINAR CLIENTE
public class VerCliente extends AppCompatActivity {

    EditText et_nombre, et_apellidos, et_dni, et_correo, et_direccion, et_telefono;
    FloatingActionButton fab_editar, fab_borrar, fab_guardar, fab_volver;
    LClientes cliente;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_cliente);

        et_nombre = findViewById(R.id.et_nombrevc);
        et_apellidos = findViewById(R.id.et_apellidosvc);
        et_dni = findViewById(R.id.et_dnivc);
        et_correo = findViewById(R.id.et_correovc);
        et_direccion = findViewById(R.id.et_direccionvc);
        et_telefono = findViewById(R.id.et_telefonovc);
        fab_editar = findViewById(R.id.fab_editarvc);
        fab_borrar = findViewById(R.id.fab_borrarvc);
        fab_volver = findViewById(R.id.fab_volvervc);
        fab_guardar = findViewById(R.id.fab_guardarvc);
        fab_guardar.setVisibility(View.INVISIBLE);

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        DBClientes dbClientes = new DBClientes(VerCliente.this);
        cliente = dbClientes.verCliente(id);

        if(cliente != null){
            et_nombre.setText(cliente.getNombre());
            et_apellidos.setText(cliente.getApellidos());
            et_dni.setText(cliente.getDni());
            et_correo.setText(cliente.getCorreo());
            et_direccion.setText(cliente.getDireccion());
            et_telefono.setText(cliente.getTelefono());

            et_nombre.setInputType(InputType.TYPE_NULL);
            et_apellidos.setInputType(InputType.TYPE_NULL);
            et_dni.setInputType(InputType.TYPE_NULL);
            et_correo.setInputType(InputType.TYPE_NULL);
            et_direccion.setInputType(InputType.TYPE_NULL);
            et_telefono.setInputType(InputType.TYPE_NULL);
        }

        //EDITAR CLIENTE
        fab_editar.setOnClickListener(v -> {
            Intent intent = new Intent(VerCliente.this,EditarCliente.class);
            intent.putExtra("ID", id);
            startActivity(intent);
            finish();
        });

        //VOLVER CLIENTE
        fab_volver.setOnClickListener(v ->{
            Intent volverc = new Intent(this, Clientes.class);
            finish();
            startActivity(volverc);
        });

        //BORRAR CLIENTE
        fab_borrar.setOnClickListener(v -> {
            String segurocc = getString(R.string.seguroc);
            String segurosicc = getString(R.string.segurosic);
            String seguronocc = getString(R.string.seguronoc);
            AlertDialog.Builder builder = new AlertDialog.Builder(VerCliente.this);
            builder.setMessage(segurocc).setPositiveButton(segurosicc, (dialog, which) -> {
                if(dbClientes.eliminarCliente(id)){
                    finish();
                    Volver();
                }
            }).setNegativeButton(seguronocc, (dialog, which) -> {

            }).show();
        });

    }

    //VOLVER
    private void Volver(){
        Intent volver = new Intent(this, Clientes.class);
        startActivity(volver);
    }

}