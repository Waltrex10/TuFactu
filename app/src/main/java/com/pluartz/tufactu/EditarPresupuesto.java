package com.pluartz.tufactu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pluartz.tufactu.db.DBPresupuesto;
import com.pluartz.tufactu.entidades.LPresupuesto;

public class EditarPresupuesto extends AppCompatActivity {

    EditText et_numero, et_fecha, et_descripcion;
    FloatingActionButton fab_editar, fab_borrar, fab_guardar, fab_volver;
    boolean correcto = false;
    LPresupuesto presupuesto;
    int id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_presupuesto);

        et_numero = findViewById(R.id.et_numerovp);
        et_fecha = findViewById(R.id.et_fechavp);
        et_descripcion = findViewById(R.id.et_descripcionvp);
        fab_editar = findViewById(R.id.fab_editarvp);
        fab_editar.setVisibility(View.INVISIBLE);
        fab_borrar = findViewById(R.id.fab_borrarvp);
        fab_borrar.setVisibility(View.INVISIBLE);
        fab_volver = findViewById(R.id.fab_volvervp);
        fab_volver.setVisibility(View.INVISIBLE);
        fab_guardar = findViewById(R.id.fab_guardarvp);

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

        DBPresupuesto dbPresupuesto = new DBPresupuesto(EditarPresupuesto.this);
        presupuesto = dbPresupuesto.verPresupuesto(id);

        if (presupuesto != null) {
            et_numero.setText(presupuesto.getNumero());
            et_fecha.setText(presupuesto.getFecha());
            et_descripcion.setText(presupuesto.getDescripcion());

        }

        fab_guardar.setOnClickListener(v -> {
            if(!et_numero.getText().toString().equals("") && !et_fecha.getText().toString().equals("")){
                correcto = dbPresupuesto.editarPresuspuesto(id, et_numero.getText().toString(), et_fecha.getText().toString(), et_descripcion.getText().toString());
                if(correcto){
                    Toast.makeText(EditarPresupuesto.this, "Registro modificado", Toast.LENGTH_LONG).show();
                    finish();
                    Volver();
                } else {
                    Toast.makeText(EditarPresupuesto.this,"Error al modificar la factura", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(EditarPresupuesto.this,"Debe llenar los campos", Toast.LENGTH_LONG).show();
            }
        });

    }

    //Volver a inventario
    private void Volver(){
        Intent intent = new Intent(this, VerPresupuesto.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

}
