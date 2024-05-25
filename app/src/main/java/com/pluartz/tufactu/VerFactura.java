package com.pluartz.tufactu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pluartz.tufactu.db.DBFactura;
import com.pluartz.tufactu.entidades.LFactura;

//VER, EDITAR Y ELIMINAR FACTURA
public class VerFactura extends AppCompatActivity {

    EditText et_numero, et_fecha, et_descripcion;
    FloatingActionButton fab_editar, fab_borrar, fab_guardar;
    LFactura factura;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_factura);

        et_numero = findViewById(R.id.et_numerovf);
        et_fecha = findViewById(R.id.et_fechavf);
        et_descripcion = findViewById(R.id.et_descripcionvf);
        fab_editar = findViewById(R.id.fab_editarvf);
        fab_borrar = findViewById(R.id.fab_borrarvf);
        fab_guardar = findViewById(R.id.fab_guardarvf);
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

        DBFactura dbFactura = new DBFactura(VerFactura.this);
        factura = dbFactura.verFactura(id);

        if (factura != null){
            et_numero.setText(factura.getNumero());
            et_fecha.setText(factura.getFecha());
            et_descripcion.setText(factura.getDescripcion());



            et_numero.setInputType(InputType.TYPE_NULL);
            et_fecha.setInputType(InputType.TYPE_NULL);
            et_descripcion.setInputType(InputType.TYPE_NULL);
        }

        //EDITAR FACTURA
        fab_editar.setOnClickListener(v -> {
            Intent intent = new Intent(VerFactura.this,EditarFactura.class);
            intent.putExtra("ID", id);
            startActivity(intent);
        });

        //BORRAR FACTURA
        fab_borrar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(VerFactura.this);
            builder.setMessage("¿Seguro que quiere borrar la factura?").setPositiveButton("SI", (dialog, which) -> {
                if(dbFactura.eliminarFactura(id)){
                    Volver();
                }
            }).setNegativeButton("NO", (dialog, which) -> {
            }).show();
        });

    }

    //VOLVER
    private void Volver(){
        Intent volver = new Intent(this, Facturas.class);
        startActivity(volver);
    }
}