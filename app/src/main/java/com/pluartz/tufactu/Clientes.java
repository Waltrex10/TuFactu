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
import com.pluartz.tufactu.db.DBClientes;
import com.pluartz.tufactu.entidades.LClientes;

import java.util.ArrayList;

//AGREGAR Y MOSTRAR CLIENTES
public class Clientes extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView buscar;
    RecyclerView listaClientes;
    ArrayList<LClientes> listaArrayClientes;
    ListaClientesAdapter adapterc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        buscar = findViewById(R.id.buscarc);
        listaClientes = findViewById(R.id.listaClientes);
        listaClientes.setLayoutManager(new LinearLayoutManager(this));

        DBClientes dbClientes = new DBClientes(Clientes.this);
        listaArrayClientes = new ArrayList<>();
        adapterc = new ListaClientesAdapter(dbClientes.mostrarClientes());
        listaClientes.setAdapter(adapterc);

        //AGREGAR CLIENTES
        FloatingActionButton fab_anadir = findViewById(R.id.fab_masc);
        fab_anadir.setOnClickListener(v -> startActivity(new Intent(Clientes.this, NuevoCliente.class)));

        buscar.setOnQueryTextListener(this);

        //MENU DE ABAJO
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_person_add);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_request_page) {
                    startActivity(new Intent(Clientes.this, Facturas.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_request_quote) {
                    startActivity(new Intent(Clientes.this, Presupuestos.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_inventory) {
                    startActivity(new Intent(Clientes.this, Inventario.class));
                    finish();
                    return true;
                } else if (itemId == R.id.navigation_person_add) {
                    return true;
                } else if (itemId == R.id.navigation_settings) {
                    startActivity(new Intent(Clientes.this, Ajustes.class));
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
        adapterc.filtradoc(s);
        return false;
    }
}