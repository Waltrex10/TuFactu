package com.pluartz.tufactu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pluartz.tufactu.db.DBFactura;
import com.pluartz.tufactu.db.DBPresupuesto;

public class NuevoPresupuesto extends AppCompatActivity {

    EditText et_numero, et_fecha, et_descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_presupuesto);

        et_numero = findViewById(R.id.et_numeronp);
        et_fecha = findViewById(R.id.et_fechanp);
        et_descripcion = findViewById(R.id.et_descripcionnp);
        Button but_guardar = findViewById(R.id.but_guardarnp);

        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        final String dni = sharedPref.getString("dniusuario", String.valueOf(-1));

        but_guardar.setOnClickListener(v -> {
            try (DBPresupuesto dbPresupuesto= new DBPresupuesto(NuevoPresupuesto.this)) {
                long id = dbPresupuesto.insertarPresupuesto(et_numero.getText().toString(), et_fecha.getText().toString(), et_descripcion.getText().toString(), dni);
                if (id > 0) {
                    Toast.makeText(NuevoPresupuesto.this, "Presupuesto guardada correctamente", Toast.LENGTH_SHORT).show();
                    limpiar();
                    Intent Presupuesto = new Intent(NuevoPresupuesto.this, Presupuestos.class);
                    startActivity(Presupuesto);
                    finish();
                } else {
                    Toast.makeText(NuevoPresupuesto.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                Toast.makeText(NuevoPresupuesto.this, "Ocurrió un error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void limpiar(){
        et_numero.setText("");
        et_fecha.setText("");
        et_descripcion.setText("");
    }

}