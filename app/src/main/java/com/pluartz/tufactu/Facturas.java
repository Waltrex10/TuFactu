package com.pluartz.tufactu;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pluartz.tufactu.adaptadores.ListaFacturaAdapter;
import com.pluartz.tufactu.db.DBFactura;
import com.pluartz.tufactu.entidades.LFactura;

import java.util.ArrayList;

//AGREGAR Y MOSTRAR FACTURA
public class Facturas extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView buscar;
    RecyclerView listaFacturas;
    ArrayList<LFactura> listaArrayFacturas;
    ListaFacturaAdapter adapterf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturas);

        buscar = findViewById(R.id.buscarf);
        listaFacturas = findViewById(R.id.listaFactura);
        listaFacturas.setLayoutManager(new LinearLayoutManager(this));

        DBFactura dbFactura = new DBFactura(Facturas.this);
        listaArrayFacturas = new ArrayList<>();
        adapterf = new ListaFacturaAdapter(dbFactura.mostrarFactura());
        listaFacturas.setAdapter(adapterf);

        //AGREGAR FACTURA
        FloatingActionButton fab_anadir = findViewById(R.id.fab_masf);
        fab_anadir.setOnClickListener(v -> startActivity(new Intent(Facturas.this, NuevaFactura.class)));

        buscar.setOnQueryTextListener(this);

        //MENU DE ABAJO
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_request_page);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_request_page) {
                    return true;
                } else if (itemId == R.id.navigation_request_quote) {
                    startActivity(new Intent(Facturas.this, Presupuestos.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_inventory) {
                    startActivity(new Intent(Facturas.this, Inventario.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_person_add) {
                    startActivity(new Intent(Facturas.this, Clientes.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_settings) {
                    startActivity(new Intent(Facturas.this, Ajustes.class));
                    finish();
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

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapterf.filtradof(s);
        return false;
    }
}
