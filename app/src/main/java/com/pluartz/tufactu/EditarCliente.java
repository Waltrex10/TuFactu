package com.pluartz.tufactu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pluartz.tufactu.db.DBClientes;
import com.pluartz.tufactu.entidades.LClientes;

public class EditarCliente extends AppCompatActivity {

    private EditText et_nombre, et_apellidos, et_dni, et_correo, et_direccion, et_telefono;
    private FloatingActionButton fab_editar, fab_borrar, fab_guardar;
    boolean correcto = false;
    LClientes cliente;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_cliente);

        et_nombre = (EditText) findViewById(R.id.et_nombrevc);
        et_apellidos = (EditText) findViewById(R.id.et_apellidosvc);
        et_dni = (EditText) findViewById(R.id.et_dnivc);
        et_correo = (EditText) findViewById(R.id.et_correovc);
        et_direccion = (EditText) findViewById(R.id.et_direccionvc);
        et_telefono = (EditText) findViewById(R.id.et_telefonovc);
        fab_editar = (FloatingActionButton) findViewById(R.id.fab_editarvc);
        fab_editar.setVisibility(View.INVISIBLE);
        fab_borrar = (FloatingActionButton) findViewById(R.id.fab_borrarvc);
        fab_borrar.setVisibility(View.INVISIBLE);
        fab_guardar = (FloatingActionButton) findViewById(R.id.fab_guardarvc);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        DBClientes dbClientes = new DBClientes(EditarCliente.this);
        cliente = dbClientes.verCliente(id);

        if (cliente != null) {
            et_nombre.setText(cliente.getNombre());
            et_apellidos.setText(cliente.getApellidos());
            et_dni.setText(cliente.getDni());
            et_correo.setText(cliente.getCorreo());
            et_direccion.setText(cliente.getDireccion());
            et_telefono.setText(cliente.getTelefono());

        }

        fab_guardar.setOnClickListener(v -> {
            if(!et_nombre.getText().toString().equals("") && !et_apellidos.getText().toString().equals("")){
                correcto = dbClientes.editarCliente(id, et_nombre.getText().toString(), et_apellidos.getText().toString(), et_dni.getText().toString(), et_correo.getText().toString(), et_direccion.getText().toString(), et_telefono.getText().toString());
                if(correcto){
                    Toast.makeText(EditarCliente.this, "Registro modificado", Toast.LENGTH_LONG).show();
                    lista();
                } else {
                    Toast.makeText(EditarCliente.this,"Error al modificar cliente", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(EditarCliente.this,"Debe llenar los campos", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void lista(){
        Intent intent = new Intent(this, VerCliente.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

}
