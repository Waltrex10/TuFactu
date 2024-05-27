package com.pluartz.tufactu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pluartz.tufactu.db.DBPresupuesto;
import com.pluartz.tufactu.entidades.LPresupuesto;

//VER, EDITAR Y ELIMINAR PRESUPUESTO
public class VerPresupuesto extends AppCompatActivity {

    EditText et_numero, et_fecha, et_descripcion;
    FloatingActionButton fab_editar, fab_borrar, fab_guardar, fab_volver;
    LPresupuesto presupuesto;
    int id = 0;

    //VER PRESUPUESTO
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_presupuesto);

        et_numero = findViewById(R.id.et_numerovp);
        et_fecha = findViewById(R.id.et_fechavp);
        et_descripcion = findViewById(R.id.et_descripcionvp);
        fab_editar = findViewById(R.id.fab_editarvp);
        fab_borrar = findViewById(R.id.fab_borrarvp);
        fab_volver = findViewById(R.id.fab_volvervp);
        fab_guardar = findViewById(R.id.fab_guardarvp);
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

        DBPresupuesto dbPresupuesto = new DBPresupuesto(VerPresupuesto.this);
        presupuesto = dbPresupuesto.verPresupuesto(id);

        if (presupuesto != null){
            et_numero.setText(presupuesto.getNumero());
            et_fecha.setText(presupuesto.getFecha());
            et_descripcion.setText(presupuesto.getDescripcion());

            et_numero.setInputType(InputType.TYPE_NULL);
            et_fecha.setInputType(InputType.TYPE_NULL);
            et_descripcion.setInputType(InputType.TYPE_NULL);
        }

        //EDITAR PRESUPUESTO
        fab_editar.setOnClickListener(v -> {
            Intent intent = new Intent(VerPresupuesto.this,EditarPresupuesto.class);
            intent.putExtra("ID", id);
            startActivity(intent);
            finish();
        });

        //VOLVER PRESUPUESTO
        fab_volver.setOnClickListener(v ->{
            Intent volverp = new Intent(this, Presupuestos.class);
            finish();
            startActivity(volverp);
        });

        //BORRAR PRESUPUESTO
        fab_borrar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerPresupuesto.this);
            builder.setMessage("¿Seguro que quiere borrar el presupuesto?").setPositiveButton("SI", (dialog, which) -> {
                if(dbPresupuesto.eliminarPresupuesto(id)){
                    finish();
                    Volver();
                }
            }).setNegativeButton("NO", (dialog, which) -> {
            }).show();
        });

    }

    //VOLVER
    private void Volver(){
        Intent volver = new Intent(this, Presupuestos.class);
        startActivity(volver);
    }

}
