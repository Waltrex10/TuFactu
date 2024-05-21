package com.pluartz.tufactu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pluartz.tufactu.db.DBInventario;

public class NuevoInventario extends AppCompatActivity {

    private EditText et_nombre, et_precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_inventario);

        et_nombre = (EditText) findViewById(R.id.et_nombreni);
        et_precio = (EditText) findViewById(R.id.et_precioni);

        Button but_guardar = (Button) findViewById(R.id.but_guardarni);

        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        final String dni = sharedPref.getString("dniusuario", String.valueOf(-1));

        but_guardar.setOnClickListener(v -> {
            try (DBInventario DBInventario = new DBInventario(NuevoInventario.this)) {
                long id = DBInventario.insertaInventario(et_nombre.getText().toString(),et_precio.getText().toString(), dni);
                if (id > 0){
                    Toast.makeText(NuevoInventario.this, "Inventario guardado", Toast.LENGTH_LONG).show();
                    limpiar();
                    Intent Inventario = new Intent(NuevoInventario.this, Inventario.class);
                    startActivity(Inventario);
                    finish();
                } else {
                    Toast.makeText(NuevoInventario.this, "Error al guardar", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e){
                Toast.makeText(NuevoInventario.this, "Ocurrió un error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void limpiar(){
        et_nombre.setText("");
        et_precio.setText("");
    }

}