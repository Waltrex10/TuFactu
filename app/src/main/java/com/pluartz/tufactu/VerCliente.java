package com.pluartz.tufactu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pluartz.tufactu.db.DBClientes;
import com.pluartz.tufactu.entidades.LClientes;

//Para ver, editar y eliminar clientes
public class VerCliente extends AppCompatActivity {

    private EditText et_nombre, et_apellidos, et_dni, et_correo, et_direccion, et_telefono;
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
        FloatingActionButton fab_editar = (FloatingActionButton) findViewById(R.id.fab_editarvc);
        FloatingActionButton fab_borrar = (FloatingActionButton) findViewById(R.id.fab_borrarvc);
        FloatingActionButton fab_guardar = (FloatingActionButton) findViewById(R.id.fab_guardarvc);

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

            fab_guardar.setVisibility(View.INVISIBLE);

            et_nombre.setInputType(InputType.TYPE_NULL);
            et_apellidos.setInputType(InputType.TYPE_NULL);
            et_dni.setInputType(InputType.TYPE_NULL);
            et_correo.setInputType(InputType.TYPE_NULL);
            et_direccion.setInputType(InputType.TYPE_NULL);
            et_telefono.setInputType(InputType.TYPE_NULL);
        }

        fab_editar.setOnClickListener(v -> {
            Intent intent = new Intent(VerCliente.this,EditarCliente.class);
            intent.putExtra("ID", id);
            startActivity(intent);
        });

        //Borrar clientes
        fab_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerCliente.this);
                builder.setMessage("¿Seguro que quiere borrar el cliente?").setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(dbClientes.eliminarCliente(id)){
                            lista();
                        }

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });

    }

    //Volver a clientes
    private void lista(){
        Intent volver = new Intent(this, Clientes.class);
        startActivity(volver);
    }

}