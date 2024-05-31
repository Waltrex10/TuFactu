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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//NUEVA FACTURA
public class NuevaFactura extends AppCompatActivity {

    EditText et_numero, et_fecha, et_descripcion;
    Spinner spinnerClientes;
    ArrayList<String> dniList, inventarioList;
    LinearLayout inventarioLayout;
    final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_factura);

        et_numero = findViewById(R.id.et_numeronf);
        et_fecha = findViewById(R.id.et_fechanf);
        et_descripcion = findViewById(R.id.et_descripcionnf);
        spinnerClientes = findViewById(R.id.spinnerclientesnf);
        inventarioLayout = findViewById(R.id.inventario_layoutf);

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

        //GUARDAR FACTURA
        Button but_guardar = findViewById(R.id.but_guardarnf);
        but_guardar.setOnClickListener(v -> {
            String dniClienteSeleccionado = (String) spinnerClientes.getSelectedItem();
            try (DBFactura dbFactura = new DBFactura(NuevaFactura.this)) {
                long id = dbFactura.insertarFactura(et_numero.getText().toString(), et_fecha.getText().toString(), et_descripcion.getText().toString(), dniClienteSeleccionado, dni);
                if (id > 0) {
                    String guardadof = getString(R.string.guardadof);
                    Toast.makeText(NuevaFactura.this, guardadof, Toast.LENGTH_SHORT).show();
                    enviaremail();
                } else {
                    String errorf = getString(R.string.errorf);
                    Toast.makeText(NuevaFactura.this, errorf, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                Toast.makeText(NuevaFactura.this, "Ocurrió un error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        agregarInventario();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean primeraSeleccion = true;


    //ZONA DEL INVENTARIO
    private void agregarInventario() {
        LinearLayout inventoryItemLayout = new LinearLayout(this);
        inventoryItemLayout.setOrientation(LinearLayout.HORIZONTAL);
        inventoryItemLayout.setPadding(0, 10, 0, 10);

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
        String preciof = getString(R.string.preciof);
        etPrecio.setHint(preciof);
        etPrecio.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etPrecio.setEnabled(false);

        EditText etCantidad = new EditText(this);
        LinearLayout.LayoutParams cantidadParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        etCantidad.setLayoutParams(cantidadParams);
        String cantidadf = getString(R.string.cantidadf);
        etCantidad.setHint(cantidadf);
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
                String codigof = getString(R.string.codigoff);
                String nombreff = getString(R.string.nombreff);
                String precioff = getString(R.string.precioff);
                String cantidadff = getString(R.string.cantidadff);
                String subtotalf = getString(R.string.subtotalf);
                inventariosInfo.append(codigof).append(" ").append(item.getId()).append(" | ")
                        .append(nombreff).append(" ").append(item.getNombre()).append(" | ")
                        .append(precioff).append(" ").append(decimalFormat.format(precio)).append("€").append(" | ")
                        .append(cantidadff).append(" ").append(decimalFormat.format(cantidad)).append("\n")
                        .append(subtotalf).append(" ").append(decimalFormat.format(subtotal)).append("€").append("\n");
            }

            double iva = total * 0.21;
            double totalConIva = total + iva;

            String datosf = getString(R.string.datosf);
            String inffa = getString(R.string.inffa);
            String fechaf = getString(R.string.fechaf);
            String nf = getString(R.string.nf);
            String descripf = getString(R.string.descripf);
            String infemf = getString(R.string.infemf);
            String infclif = getString(R.string.infclif);
            String prof = getString(R.string.prof);
            String totalf = getString(R.string.totalf);
            String ivaf = getString(R.string.ivaf);
            String totalivaf = getString(R.string.totalivaf);

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{usuario.getCorreo(),clientes.getCorreo()});
            intent.putExtra(Intent.EXTRA_SUBJECT, datosf);
            intent.putExtra(Intent.EXTRA_TEXT,
                    inffa + "\n" +
                            nf + " " + et_numero.getText().toString() + "\n" +
                            fechaf + " " + et_fecha.getText().toString() + "\n" +
                            descripf + " " + et_descripcion.getText().toString() + "\n\n" +
                            infemf + " " + "\n" +
                            usuario.getNombre() + " " + usuario.getApellidos() + "\n" +
                            usuario.getDni() + "\n" +
                            usuario.getDireccion() + "\n" +
                            usuario.getPostal() + "\n\n" +
                            infclif + "\n" +
                            clientes.getNombre() + " " + clientes.getApellidos() + "\n" +
                            clientes.getDni() + "\n" +
                            clientes.getDireccion() + "\n\n" +
                            prof + "\n" +
                            inventariosInfo +
                            "\n" + totalf + " " + decimalFormat.format(total) + "€ \n" +
                            ivaf + " " + decimalFormat.format(iva) + "€" + "\n" +
                            totalivaf + " " + decimalFormat.format(totalConIva) + "€"
            );

            try {
                String enviarcorreo = getString(R.string.enviarcorreo);
                startActivity(Intent.createChooser(intent, enviarcorreo));
            } catch (android.content.ActivityNotFoundException ex) {
                String nohaycli = getString(R.string.nohaycli);
                Toast.makeText(this, nohaycli, Toast.LENGTH_SHORT).show();
            }
        } else {
            String errorf1 = getString(R.string.errorf1);
            Toast.makeText(this, errorf1, Toast.LENGTH_SHORT).show();
        }
    }

}