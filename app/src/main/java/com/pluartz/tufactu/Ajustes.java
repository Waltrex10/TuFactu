package com.pluartz.tufactu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//Ventana de ajustes
public class Ajustes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        Button but_ajustes = findViewById(R.id.but_ajustes);
        Button but_cerrarsesion = findViewById(R.id.but_cerrarsesion);

        //Boton para ir a ajustes de usuario
        but_ajustes.setOnClickListener(v -> startActivity(new Intent(Ajustes.this, AjustesUsuario.class)));
        //Boton para cerrar sesion
        but_cerrarsesion.setOnClickListener(v -> {
            startActivity(new Intent(Ajustes.this, MainActivity.class));
            finish();
        });

        //Menu de abajo
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_request_page) {
                    startActivity(new Intent(Ajustes.this, Facturas.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_request_quote) {
                    startActivity(new Intent(Ajustes.this, Presupuestos.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_inventory) {
                    startActivity(new Intent(Ajustes.this, Inventario.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_person_add) {
                    startActivity(new Intent(Ajustes.this, Clientes.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_settings) {
                    return true;
                }
                return false;
            }
        });
    }
    private static final int NAVIGATION_REQUEST_PAGE_ID = R.id.navigation_request_page;
    private static final int NAVIGATION_REQUEST_QUOTE_ID = R.id.navigation_request_quote;
    private static final int NAVIGATION_INVENTORY_ID = R.id.navigation_inventory;
    private static final int NAVIGATION_PERSON_ADD_ID = R.id.navigation_person_add;
    private static final int NAVIGATION_SETTINGS_ID = R.id.navigation_settings;
}