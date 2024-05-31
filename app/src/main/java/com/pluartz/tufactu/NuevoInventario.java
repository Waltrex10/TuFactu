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

//NUEVO INVENTARIO
public class NuevoInventario extends AppCompatActivity {

    EditText et_nombre, et_precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_inventario);

        et_nombre = findViewById(R.id.et_nombreni);
        et_precio = findViewById(R.id.et_precioni);
        Button but_guardar = findViewById(R.id.but_guardarni);

        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        final String dni = sharedPref.getString("dniusuario", String.valueOf(-1));

        //GUARDAR
        but_guardar.setOnClickListener(v -> {
            try (DBInventario DBInventario = new DBInventario(NuevoInventario.this)) {
                long id = DBInventario.insertaInventario(et_nombre.getText().toString(),et_precio.getText().toString(), dni);
                if (id > 0){
                    String guardadoi = getString(R.string.guardadoi);
                    Toast.makeText(NuevoInventario.this, guardadoi, Toast.LENGTH_SHORT).show();
                    limpiar();
                    Intent Inventario = new Intent(NuevoInventario.this, Inventario.class);
                    startActivity(Inventario);
                    finish();
                } else {
                    String errori = getString(R.string.errori);
                    Toast.makeText(NuevoInventario.this, errori, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                Toast.makeText(NuevoInventario.this, "Ocurrió un error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void limpiar(){
        et_nombre.setText("");
        et_precio.setText("");
    }

}