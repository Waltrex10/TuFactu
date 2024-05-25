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

public class NuevaFactura extends AppCompatActivity {

    EditText et_numero, et_fecha, et_descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_factura);

        et_numero = findViewById(R.id.et_numeronf);
        et_fecha = findViewById(R.id.et_fechanf);
        et_descripcion = findViewById(R.id.et_descripcionnf);
        Button but_guardar = findViewById(R.id.but_guardarnf);

        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        final String dni = sharedPref.getString("dniusuario", String.valueOf(-1));

        but_guardar.setOnClickListener(v -> {
            try (DBFactura dbFactura = new DBFactura(NuevaFactura.this)) {
                long id = dbFactura.insertarFactura(et_numero.getText().toString(), et_fecha.getText().toString(), et_descripcion.getText().toString(), dni);
                if (id > 0) {
                    Toast.makeText(NuevaFactura.this, "Factura guardada correctamente", Toast.LENGTH_SHORT).show();
                    limpiar();
                    Intent Factura = new Intent(NuevaFactura.this, Facturas.class);
                    startActivity(Factura);
                    finish();
                } else {
                    Toast.makeText(NuevaFactura.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                Toast.makeText(NuevaFactura.this, "Ocurrió un error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void limpiar(){
        et_numero.setText("");
        et_fecha.setText("");
        et_descripcion.setText("");
    }

}