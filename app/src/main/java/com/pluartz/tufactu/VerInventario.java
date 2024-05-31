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

//VER, EDITAR Y ELIMINAR INVENTARIO
public class VerInventario extends AppCompatActivity {

    EditText et_nombre, et_precio;
    FloatingActionButton fab_editar, fab_borrar, fab_guardar, fab_volver;
    LInventario inventario;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_inventario);

        et_nombre = findViewById(R.id.et_nombrevi);
        et_precio = findViewById(R.id.et_preciovi);
        fab_editar = findViewById(R.id.fab_editarvi);
        fab_borrar = findViewById(R.id.fab_borrarvi);
        fab_volver = findViewById(R.id.fab_volvervi);
        fab_guardar = findViewById(R.id.fab_guardarvi);
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

        DBInventario dbInventario = new DBInventario(VerInventario.this);
        inventario = dbInventario.verInventario(id);

        if (inventario != null) {
            et_nombre.setText(inventario.getNombre());
            et_precio.setText(inventario.getPrecio());

            et_nombre.setInputType(InputType.TYPE_NULL);
            et_precio.setInputType(InputType.TYPE_NULL);
        }

        //EDIRAR INVENTARIO
        fab_editar.setOnClickListener(v -> {
            Intent intent = new Intent(VerInventario.this, EditarInventario.class);
            intent.putExtra("ID", id);
            startActivity(intent);
            finish();
        });

        //VOLVER INVENTARIO
        fab_volver.setOnClickListener(v ->{
            Intent volveri = new Intent(this, Inventario.class);
            finish();
            startActivity(volveri);
        });

        //BORRAR INVENTARIO
        fab_borrar.setOnClickListener(v -> {
            String seguroi = getString(R.string.seguroi);
            String segurosii = getString(R.string.segurosii);
            String seguronoi = getString(R.string.seguronoi);
            AlertDialog.Builder builder = new AlertDialog.Builder(VerInventario.this);
            builder.setMessage(seguroi).setPositiveButton(segurosii, (dialog, which) -> {
                if (dbInventario.eliminarInventario(id)) {
                    finish();
                    Volver();
                }
            }).setNegativeButton(seguronoi, (dialog, which) -> {
            }).show();
        });


    }

    //VOLVER
    private void Volver(){
        Intent volver = new Intent(this, Inventario.class);
        startActivity(volver);
    }

}