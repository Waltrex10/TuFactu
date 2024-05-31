package com.pluartz.tufactu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pluartz.tufactu.db.DBInventario;
import com.pluartz.tufactu.entidades.LInventario;

//EDITAR INVENTARIO
public class EditarInventario extends AppCompatActivity {

    EditText et_nombre, et_precio;
    FloatingActionButton fab_editar, fab_borrar, fab_guardar, fab_volver;

    boolean correcto = false;
    LInventario inventario;
    int id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_inventario);

        et_nombre = findViewById(R.id.et_nombrevi);
        et_precio = findViewById(R.id.et_preciovi);
        fab_editar = findViewById(R.id.fab_editarvi);
        fab_editar.setVisibility(View.INVISIBLE);
        fab_borrar = findViewById(R.id.fab_borrarvi);
        fab_borrar.setVisibility(View.INVISIBLE);
        fab_volver = findViewById(R.id.fab_volvervi);
        fab_volver.setVisibility(View.INVISIBLE);
        fab_guardar = findViewById(R.id.fab_guardarvi);

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

        DBInventario dbInventario = new DBInventario(EditarInventario.this);
        inventario = dbInventario.verInventario(id);

        if (inventario != null) {
            et_nombre.setText(inventario.getNombre());
            et_precio.setText(inventario.getPrecio());

        }

        //GUARDAR
        fab_guardar.setOnClickListener(v -> {
            if(!et_nombre.getText().toString().equals("")){
                correcto = dbInventario.editarInventario(id, et_nombre.getText().toString(), et_precio.getText().toString());
                if(correcto){
                    String remoi = getString(R.string.remoi);
                    Toast.makeText(EditarInventario.this, remoi, Toast.LENGTH_LONG).show();
                    finish();
                    Volver();
                } else {
                    String moinv = getString(R.string.moinv);
                    Toast.makeText(EditarInventario.this,moinv, Toast.LENGTH_LONG).show();
                }
            } else {
                String llenarinv = getString(R.string.llenarinv);
                Toast.makeText(EditarInventario.this,llenarinv, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void Volver(){
        Intent intent = new Intent(this, VerInventario.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

}