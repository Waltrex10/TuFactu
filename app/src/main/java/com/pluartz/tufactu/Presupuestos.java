package com.pluartz.tufactu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pluartz.tufactu.adaptadores.ListaClientesAdapter;
import com.pluartz.tufactu.adaptadores.ListaPresupuestoAdapter;
import com.pluartz.tufactu.db.DBPresupuesto;
import com.pluartz.tufactu.entidades.LPresupuesto;

import java.util.ArrayList;

public class Presupuestos extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView buscar;
    RecyclerView listaPresupuestos;
    ArrayList<LPresupuesto> listaArrayPresupuestos;
    ListaPresupuestoAdapter adapterp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuestos);

        buscar = findViewById(R.id.buscarp);
        listaPresupuestos = findViewById(R.id.listaPresupuesto);
        listaPresupuestos.setLayoutManager(new LinearLayoutManager(this));

        DBPresupuesto dbPresupuesto = new DBPresupuesto(Presupuestos.this);
        listaArrayPresupuestos = new ArrayList<>();
        adapterp = new ListaPresupuestoAdapter(dbPresupuesto.mostrarPresupuesto());
        listaPresupuestos.setAdapter(adapterp);

        FloatingActionButton fab_anadir = findViewById(R.id.fab_masp);
        fab_anadir.setOnClickListener(v -> startActivity(new Intent(Presupuestos.this, NuevoPresupuesto.class)));

        buscar.setOnQueryTextListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_request_quote);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_request_page) {
                    startActivity(new Intent(Presupuestos.this, Facturas.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_request_quote) {
                    return true;
                } else if (itemId == R.id.navigation_inventory) {
                    startActivity(new Intent(Presupuestos.this, Inventario.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_person_add) {
                    startActivity(new Intent(Presupuestos.this, Clientes.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_settings) {
                    startActivity(new Intent(Presupuestos.this, Ajustes.class));
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
        adapterp.filtradop(s);
        return false;
    }
}