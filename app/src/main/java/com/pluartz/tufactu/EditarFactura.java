package com.pluartz.tufactu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pluartz.tufactu.db.DBClientes;
import com.pluartz.tufactu.db.DBFactura;
import com.pluartz.tufactu.entidades.LFactura;

import java.util.ArrayList;

public class EditarFactura extends AppCompatActivity {

    EditText et_numero, et_fecha, et_descripcion;
    FloatingActionButton fab_editar, fab_borrar, fab_guardar, fab_volver;
    Spinner spinnerDni;
    ArrayList<String> dniList;
    boolean correcto = false;
    LFactura factura;
    int id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_factura);

        et_numero = findViewById(R.id.et_numerovf);
        et_fecha = findViewById(R.id.et_fechavf);
        et_descripcion = findViewById(R.id.et_descripcionvf);
        spinnerDni = findViewById(R.id.spinnervf);
        fab_editar = findViewById(R.id.fab_editarvf);
        fab_editar.setVisibility(View.INVISIBLE);
        fab_borrar = findViewById(R.id.fab_borrarvf);
        fab_borrar.setVisibility(View.INVISIBLE);
        fab_volver = findViewById(R.id.fab_volvervf);
        fab_volver.setVisibility(View.INVISIBLE);
        fab_guardar = findViewById(R.id.fab_guardarvf);

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

        DBClientes dbClientes = new DBClientes(this);
        dniList = dbClientes.obtenerDniClientes();

        DBFactura dbFactura = new DBFactura(EditarFactura.this);
        factura = dbFactura.verFactura(id);

        if (factura != null) {
            et_numero.setText(factura.getNumero());
            et_fecha.setText(factura.getFecha());
            et_descripcion.setText(factura.getDescripcion());

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, dniList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerDni.setAdapter(adapter);

        int position = dniList.indexOf(factura.getDnicliente());
        if (position != -1) {
            spinnerDni.setSelection(position);
        }

        fab_guardar.setOnClickListener(v -> {
            String dniClienteSeleccionado = (String) spinnerDni.getSelectedItem();
            if(!et_numero.getText().toString().equals("") && !et_fecha.getText().toString().equals("")){
                correcto = dbFactura.editarFactura(id, et_numero.getText().toString(), et_fecha.getText().toString(), et_descripcion.getText().toString(), dniClienteSeleccionado);
                if(correcto){
                    Toast.makeText(EditarFactura.this, "Registro modificado", Toast.LENGTH_LONG).show();
                    finish();
                    Volver();
                } else {
                    Toast.makeText(EditarFactura.this,"Error al modificar la factura", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(EditarFactura.this,"Debe llenar los campos", Toast.LENGTH_LONG).show();
            }
        });

    }

    //Volver a inventario
    private void Volver(){
        Intent intent = new Intent(this, VerFactura.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

}