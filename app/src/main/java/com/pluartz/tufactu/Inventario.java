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
import com.pluartz.tufactu.adaptadores.ListaInventarioAdapter;
import com.pluartz.tufactu.db.DBInventario;
import com.pluartz.tufactu.entidades.LInventario;

import java.util.ArrayList;

public class Inventario extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView buscar;
    RecyclerView listaInventario;
    ArrayList<LInventario> listaArrayInventario;
    ListaInventarioAdapter adapteri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        buscar = findViewById(R.id.buscari);
        listaInventario = findViewById(R.id.listaInventario);
        listaInventario.setLayoutManager(new LinearLayoutManager(this));

        try (DBInventario dbInventario = new DBInventario(Inventario.this)){
            listaArrayInventario = new ArrayList<>();
            adapteri = new ListaInventarioAdapter(dbInventario.mostrarInventario());
            listaInventario.setAdapter(adapteri);

            FloatingActionButton fab_anadir = findViewById(R.id.fab_masi);
            fab_anadir.setOnClickListener(v -> startActivity(new Intent(Inventario.this, NuevoInventario.class)));

            buscar.setOnQueryTextListener(this);

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setSelectedItemId(R.id.navigation_inventory);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.navigation_request_page) {
                        startActivity(new Intent(Inventario.this, Facturas.class));
                        finish();
                        return true;
                    } else if (itemId == R.id.navigation_request_quote) {
                        startActivity(new Intent(Inventario.this, Presupuestos.class));
                        finish();
                        return true;
                    } else if (itemId == R.id.navigation_inventory) {
                        return true;
                    } else if (itemId == R.id.navigation_person_add) {
                        startActivity(new Intent(Inventario.this, Clientes.class));
                        finish();
                        return true;
                    } else if (itemId == R.id.navigation_settings) {
                        startActivity(new Intent(Inventario.this, Ajustes.class));
                        finish();
                        return true;
                    }
                    return false;
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
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
        adapteri.filtradoi(s);
        return false;
    }
}