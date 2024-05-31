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

//EDITAR CLIENTE
public class EditarCliente extends AppCompatActivity {

    EditText et_nombre, et_apellidos, et_dni, et_correo, et_telefono, et_direccion;
    FloatingActionButton fab_editar, fab_borrar, fab_guardar, fab_volver;
    boolean correcto = false;
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
        et_telefono = findViewById(R.id.et_telefonovc);
        et_direccion = findViewById(R.id.et_direccionvc);
        fab_editar = findViewById(R.id.fab_editarvc);
        fab_editar.setVisibility(View.INVISIBLE);
        fab_borrar = findViewById(R.id.fab_borrarvc);
        fab_borrar.setVisibility(View.INVISIBLE);
        fab_volver = findViewById(R.id.fab_volvervc);
        fab_volver.setVisibility(View.INVISIBLE);
        fab_guardar = findViewById(R.id.fab_guardarvc);

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
            et_telefono.setText(cliente.getTelefono());
            et_direccion.setText(cliente.getDireccion());

        }

        //GUARDAR
        fab_guardar.setOnClickListener(v -> {
            if(!et_nombre.getText().toString().equals("") && !et_apellidos.getText().toString().equals("")){
                correcto = dbClientes.editarCliente(id, et_nombre.getText().toString(), et_apellidos.getText().toString(), et_dni.getText().toString(), et_correo.getText().toString(), et_telefono.getText().toString(), et_direccion.getText().toString());
                if(correcto){
                    String remoc = getString(R.string.remoc);
                    Toast.makeText(EditarCliente.this, remoc, Toast.LENGTH_LONG).show();
                    finish();
                    Volver();
                } else {
                    String mocli = getString(R.string.mocli);
                    Toast.makeText(EditarCliente.this,mocli, Toast.LENGTH_LONG).show();
                }
            } else {
                String llenarcli = getString(R.string.llenarcli);
                Toast.makeText(EditarCliente.this,llenarcli, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void Volver(){
        Intent intent = new Intent(this, VerCliente.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

}
