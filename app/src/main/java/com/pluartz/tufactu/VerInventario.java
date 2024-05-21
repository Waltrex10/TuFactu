package com.pluartz.tufactu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pluartz.tufactu.db.DBInventario;
import com.pluartz.tufactu.entidades.LInventario;

//Para ver, editar y eliminar inventario
public class VerInventario extends AppCompatActivity {

    private EditText et_nombre, et_precio;

    LInventario inventario;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_inventario);

        et_nombre = (EditText) findViewById(R.id.et_nombrevi);
        et_precio = (EditText) findViewById(R.id.et_preciovi);

        FloatingActionButton fab_editar = (FloatingActionButton) findViewById(R.id.fab_editarvi);
        FloatingActionButton fab_borrar = (FloatingActionButton) findViewById(R.id.fab_borrarvi);
        FloatingActionButton fab_guardar = (FloatingActionButton) findViewById(R.id.fab_guardarvi);


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

        DBInventario dbInventario = new DBInventario(VerInventario.this);
        inventario = dbInventario.verInventario(id);

        if (inventario != null) {
            et_nombre.setText(inventario.getNombre());
            et_precio.setText(inventario.getPrecio());

            fab_guardar.setVisibility(View.INVISIBLE);

            et_nombre.setInputType(InputType.TYPE_NULL);
            et_precio.setInputType(InputType.TYPE_NULL);
        }

        //Acceder a editar inventario
        fab_editar.setOnClickListener(v -> {
            Intent intent = new Intent(VerInventario.this, EditarInventario.class);
            intent.putExtra("ID", id);
            startActivity(intent);
        });

        //Borrar inventario
        fab_borrar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerInventario.this);
            builder.setMessage("¿Seguro que quiere eliminar el producto?").setPositiveButton("SI", (dialog, which) -> {
                if (dbInventario.eliminarInventario(id)) {
                    lista();
                }

            }).setNegativeButton("NO", (dialog, which) -> {
            }).show();
        });


    }

    //Volver a inventario
    private void lista(){
        Intent volver = new Intent(this, Inventario.class);
        startActivity(volver);
    }

}