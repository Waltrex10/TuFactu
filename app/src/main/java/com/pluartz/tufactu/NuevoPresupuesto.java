package com.pluartz.tufactu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.DecimalFormat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pluartz.tufactu.db.DBClientes;
import com.pluartz.tufactu.db.DBInventario;
import com.pluartz.tufactu.db.DBPresupuesto;
import com.pluartz.tufactu.db.DBUsuario;
import com.pluartz.tufactu.entidades.LClientes;
import com.pluartz.tufactu.entidades.LInventario;
import com.pluartz.tufactu.entidades.LUsuario;

import java.util.ArrayList;
import java.util.List;

//NUEVO PRESUPUESTO
public class NuevoPresupuesto extends AppCompatActivity {

    EditText et_numero, et_fecha, et_descripcion;
    Spinner spinnerClientes;
    ArrayList<String> dniList, inventarioList;
    LinearLayout inventarioLayout;
    final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_presupuesto);

        et_numero = findViewById(R.id.et_numeronp);
        et_fecha = findViewById(R.id.et_fechanp);
        et_descripcion = findViewById(R.id.et_descripcionnp);
        spinnerClientes = findViewById(R.id.spinnerclientesnp);
        inventarioLayout = findViewById(R.id.inventario_layoutp);

        DBClientes dbClientes = new DBClientes(this);
        dniList = dbClientes.obtenerDniClientes();

        DBInventario dbInventario = new DBInventario(this);
        inventarioList = dbInventario.obtenerNombresInventario();

        inventarioList.add(0, "");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, dniList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerClientes.setAdapter(adapter);

        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        final String dni = sharedPref.getString("dniusuario", String.valueOf(-1));

        //GUARDAR PRESUPUESTO
        Button but_guardar = findViewById(R.id.but_guardarnp);
        but_guardar.setOnClickListener(v -> {
            String dniClienteSeleccionado = (String) spinnerClientes.getSelectedItem();
            try (DBPresupuesto dbPresupuesto = new DBPresupuesto(NuevoPresupuesto.this)) {
                long id = dbPresupuesto.insertarPresupuesto(et_numero.getText().toString(), et_fecha.getText().toString(), et_descripcion.getText().toString(), dniClienteSeleccionado, dni);
                if (id > 0) {
                    String guardadop = getString(R.string.guardadop);
                    Toast.makeText(NuevoPresupuesto.this, guardadop, Toast.LENGTH_SHORT).show();
                    Volver();
                    enviaremail();
                } else {
                    String errorp = getString(R.string.errorp);
                    Toast.makeText(NuevoPresupuesto.this, errorp, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                Toast.makeText(NuevoPresupuesto.this, "Ocurrió un error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        agregarInventario();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean primeraSeleccion = true;


    //ZONA DEL INVENTARIO
    private void agregarInventario() {
        LinearLayout inventoryItemLayout = new LinearLayout(this);
        inventoryItemLayout.setOrientation(LinearLayout.HORIZONTAL);

        Spinner spinnerInventario = new Spinner(this);
        LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        spinnerInventario.setLayoutParams(spinnerParams);
        ArrayAdapter<String> adapterInventario = new ArrayAdapter<>(this, R.layout.spinner_item, inventarioList);
        adapterInventario.setDropDownViewResource(R.layout.spinner_item);
        spinnerInventario.setAdapter(adapterInventario);

        EditText etPrecio = new EditText(this);
        LinearLayout.LayoutParams precioParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        etPrecio.setLayoutParams(precioParams);
        String preciop = getString(R.string.preciop);
        etPrecio.setHint(preciop);
        etPrecio.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etPrecio.setEnabled(false);

        EditText etCantidad = new EditText(this);
        LinearLayout.LayoutParams cantidadParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        etCantidad.setLayoutParams(cantidadParams);
        String cantidadp = getString(R.string.cantidadp);
        etCantidad.setHint(cantidadp);
        etCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);

        spinnerInventario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (primeraSeleccion) {
                    primeraSeleccion = false;
                    return;
                }
                if (position != 0) {
                    agregarInventario();
                }
                String selectedProduct = (String) parent.getItemAtPosition(position);
                String precioProducto = obtenerPrecioProducto(selectedProduct);
                etPrecio.setText(precioProducto);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        inventoryItemLayout.addView(spinnerInventario);
        inventoryItemLayout.addView(etPrecio);
        inventoryItemLayout.addView(etCantidad);

        inventarioLayout.addView(inventoryItemLayout);
    }

    //PRECIO PRODUCTO
    private String obtenerPrecioProducto(String nombreInventario) {
        String precio = "";
        DBInventario dbInventario = new DBInventario(this);
        LInventario inventario = dbInventario.obtenerInventarioPorNombre(nombreInventario);
        if (inventario != null) {
            precio = inventario.getPrecio();
        }
        return precio;
    }

    //SELECCONAR EL PRODUCTO "INVENTARIO"
    private List<LInventario> getSelectedInventarios() {
        List<LInventario> inventarios = new ArrayList<>();
        for (int i = 0; i < inventarioLayout.getChildCount(); i++) {
            View view = inventarioLayout.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout layout = (LinearLayout) view;
                Spinner spinner = (Spinner) layout.getChildAt(0);
                EditText etPrecio = (EditText) layout.getChildAt(1);
                EditText etCantidad = (EditText) layout.getChildAt(2);

                String nombre = (String) spinner.getSelectedItem();
                if (nombre == null || nombre.isEmpty()) {
                    continue;
                }

                String precioStr = etPrecio.getText().toString();
                String cantidadStr = etCantidad.getText().toString();

                DBInventario dbInventario = new DBInventario(this);
                LInventario inventario = dbInventario.obtenerInventarioPorNombre(nombre);
                if (inventario != null) {
                    inventario.setCantidad(cantidadStr);
                    inventario.setPrecio(precioStr);
                    inventarios.add(inventario);
                }
            }
        }
        return inventarios;
    }

    //ENVIAR CORREO
    private void enviaremail() {
        List<LInventario> inventarios = getSelectedInventarios();
        String dniClienteSeleccionado = (String) spinnerClientes.getSelectedItem();

        DBClientes dbClientes = new DBClientes(this);
        LClientes clientes = dbClientes.obtenerClientePorDNI(dniClienteSeleccionado);
        DBUsuario dbUsuario = new DBUsuario(this);

        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String dni = sharedPref.getString("dniusuario", "");
        LUsuario usuario = dbUsuario.verUsuario(dni);

        if (usuario != null && clientes != null && usuario.getCorreo() != null && !usuario.getCorreo().isEmpty()) {
            StringBuilder inventariosInfo = new StringBuilder();
            double total = 0.00;
            DecimalFormat decimalFormat = new DecimalFormat("#.00");

            for (LInventario item : inventarios) {
                double precio = Double.parseDouble(item.getPrecio());
                double cantidad = Double.parseDouble(item.getCantidad());
                double subtotal = precio * cantidad;
                total += subtotal;

                String codigop = getString(R.string.codigofp);
                String nombrefp = getString(R.string.nombrefp);
                String preciofp = getString(R.string.preciofp);
                String cantidadfp = getString(R.string.cantidadfp);
                String subtotalp = getString(R.string.subtotalp);

                inventariosInfo.append(codigop).append(" ").append(item.getId()).append(" | ")
                        .append(nombrefp).append(" ").append(item.getNombre()).append(" | ")
                        .append(preciofp).append(" ").append(decimalFormat.format(precio)).append("€").append(" | ")
                        .append(cantidadfp).append(" ").append(decimalFormat.format(cantidad)).append("\n")
                        .append(subtotalp).append(" ").append(decimalFormat.format(subtotal)).append("€").append("\n");
            }

            double iva = total * 0.21;
            double totalConIva = total + iva;

            String datosp = getString(R.string.datosp);
            String infpre = getString(R.string.infpre);
            String fechap = getString(R.string.fechap);
            String np = getString(R.string.np);
            String descripp = getString(R.string.descripp);
            String infemp = getString(R.string.infemp);
            String infclip = getString(R.string.infclip);
            String prop = getString(R.string.prop);
            String totalp = getString(R.string.totalp);
            String ivap = getString(R.string.ivap);
            String totalivap = getString(R.string.totalivap);

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{usuario.getCorreo(), clientes.getCorreo()});
            intent.putExtra(Intent.EXTRA_SUBJECT, datosp);
            intent.putExtra(Intent.EXTRA_TEXT,
                    infpre + "\n" +
                            np + " " + et_numero.getText().toString() + "\n" +
                            fechap + " " + et_fecha.getText().toString() + "\n" +
                            descripp + " " + et_descripcion.getText().toString() + "\n\n" +
                            infemp + " " + "\n" +
                            usuario.getNombre() + " " + usuario.getApellidos() + "\n" +
                            usuario.getDni() + "\n" +
                            usuario.getDireccion() + "\n" +
                            usuario.getPostal() + "\n\n" +
                            infclip + "\n" +
                            clientes.getNombre() + " " + clientes.getApellidos() + "\n" +
                            clientes.getDni() + "\n" +
                            clientes.getDireccion() + "\n\n" +
                            prop + "\n" +
                            inventariosInfo +
                            "\n" + totalp + " " + decimalFormat.format(total) + "€ \n" +
                            ivap + " " + decimalFormat.format(iva) + "€" + "\n" +
                            totalivap + " " + decimalFormat.format(totalConIva) + "€"
            );

            try {
                String enviarcorreop = getString(R.string.enviarcorreop);
                startActivity(Intent.createChooser(intent, enviarcorreop));
            } catch (android.content.ActivityNotFoundException ex) {
                String nohayclip = getString(R.string.nohayclip);
                Toast.makeText(this, nohayclip, Toast.LENGTH_SHORT).show();
            }
        } else {
            String errorp1 = getString(R.string.errorp1);
            Toast.makeText(this, errorp1, Toast.LENGTH_SHORT).show();
        }
    }

    private void Volver(){
        Intent intent = new Intent(this, Presupuestos.class);
        startActivity(intent);
    }

}