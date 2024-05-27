package com.pluartz.tufactu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pluartz.tufactu.db.DBClientes;
import com.pluartz.tufactu.db.DBFactura;
import com.pluartz.tufactu.db.DBInventario;
import com.pluartz.tufactu.db.DBUsuario;
import com.pluartz.tufactu.entidades.LClientes;
import com.pluartz.tufactu.entidades.LInventario;
import com.pluartz.tufactu.entidades.LUsuario;

import java.util.ArrayList;
import java.util.List;

public class NuevaFactura extends AppCompatActivity {

    EditText et_numero, et_fecha, et_descripcion;
    Spinner spinnerClientes;
    ArrayList<String> dniList, inventarioList;
    LinearLayout inventarioLayout;
    Spinner lastSpinner = null;
    final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_factura);

        et_numero = findViewById(R.id.et_numeronf);
        et_fecha = findViewById(R.id.et_fechanf);
        et_descripcion = findViewById(R.id.et_descripcionnf);
        spinnerClientes = findViewById(R.id.spinnerclientesnf);
        inventarioLayout = findViewById(R.id.inventario_layout);
        Button but_guardar = findViewById(R.id.but_guardarnf);


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

        but_guardar.setOnClickListener(v -> {
            String dniClienteSeleccionado = (String) spinnerClientes.getSelectedItem();
            try (DBFactura dbFactura = new DBFactura(NuevaFactura.this)) {
                long id = dbFactura.insertarFactura(et_numero.getText().toString(), et_fecha.getText().toString(), et_descripcion.getText().toString(), dniClienteSeleccionado, dni);
                if (id > 0) {
                    Toast.makeText(NuevaFactura.this, "Factura guardada correctamente", Toast.LENGTH_SHORT).show();
                    enviaremail();
                } else {
                    Toast.makeText(NuevaFactura.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                Toast.makeText(NuevaFactura.this, "Ocurrió un error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        agregarSpinnerInventario();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean primeraSeleccion = true;


    private void agregarSpinnerInventario() {
        LinearLayout inventoryItemLayout = new LinearLayout(this);
        inventoryItemLayout.setOrientation(LinearLayout.HORIZONTAL);

        Spinner spinnerInventario = new Spinner(this);
        ArrayAdapter<String> adapterInventario = new ArrayAdapter<>(this, R.layout.spinner_item, inventarioList);
        adapterInventario.setDropDownViewResource(R.layout.spinner_item);
        spinnerInventario.setAdapter(adapterInventario);

        EditText etPrecio = new EditText(this);
        etPrecio.setHint("Precio");
        etPrecio.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etPrecio.setEnabled(false);

        EditText etCantidad = new EditText(this);
        etCantidad.setHint("Cantidad");
        etCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);

        spinnerInventario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (primeraSeleccion) {
                    primeraSeleccion = false;
                    return; // No crear un nuevo spinner en la primera selección
                }

                if (position != 0) {
                    // Si la selección no es la primera opción, agregamos un nuevo spinner
                    agregarSpinnerInventario();
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

    private String obtenerPrecioProducto(String nombreInventario) {
        String precio = ""; // Precio inicialmente vacío
        // Crear una instancia de DBInventario para acceder a la base de datos
        DBInventario dbInventario = new DBInventario(this);
        // Obtener el precio del inventario desde la base de datos
        LInventario inventario = dbInventario.obtenerInventarioPorNombre(nombreInventario);
        if (inventario != null) {
            precio = inventario.getPrecio(); // Obtener el precio del inventario
        }
        return precio;
    }

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
                    continue; // Omitir si no se ha seleccionado ningún inventario
                }

                String precioStr = etPrecio.getText().toString();
                String cantidadStr = etCantidad.getText().toString();

                // Obtener el inventario usando el nombre
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
            for (LInventario item : inventarios) {
                double precio = Double.parseDouble(item.getPrecio());
                double cantidad = Double.parseDouble(item.getCantidad());
                double subtotal = precio * cantidad;
                total += subtotal;
                inventariosInfo.append("Nombre: ").append(item.getNombre())
                        .append(" | Precio: ").append(item.getPrecio()).append("€")
                        .append(" | Cantidad: ").append(item.getCantidad()).append("\n")
                        .append("Subtotal: ").append(subtotal).append("€").append("\n");
            }

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{usuario.getCorreo()});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Datos de la factura");
            intent.putExtra(Intent.EXTRA_TEXT,
                    "Información de la factura:\n" +
                            "Fecha: " + et_fecha.getText().toString() + "\n" +
                            "Nº de factura: " + et_numero.getText().toString() + "\n" +
                            "Descripción: " + et_descripcion.getText().toString() + "\n\n" +
                            "Información del proveedor:\n" +
                            usuario.getNombre() + " " + usuario.getApellidos() + "\n" +
                            usuario.getDni() + "\n" +
                            usuario.getDireccion() + "\n" +
                            usuario.getPostal() + "\n\n" +
                            "Información del cliente:\n" +
                            clientes.getNombre() + " " + clientes.getApellidos() + "\n" +
                            clientes.getDni() + "\n\n" +
                            "Productos:\n" +
                            inventariosInfo.toString() +
                            "\nTotal: " + total + "€"
            );

            try {
                startActivity(Intent.createChooser(intent, "Enviar correo usando"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "No hay clientes de correo instalados.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se pudo obtener la información del usuario o cliente", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiar(){
        et_numero.setText("");
        et_fecha.setText("");
        et_descripcion.setText("");
        inventarioLayout.removeAllViews();
        agregarSpinnerInventario();
    }
}