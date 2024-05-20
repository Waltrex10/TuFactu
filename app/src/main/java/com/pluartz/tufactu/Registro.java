package com.pluartz.tufactu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pluartz.tufactu.db.DBUsuario;

import java.io.ByteArrayOutputStream;

//Realizar el registro del usuario
public class Registro extends AppCompatActivity {

    private EditText et_nombre, et_apellidos, et_dni, et_mail, et_contrasena, et_concontrasena, et_nombreempresa, et_telefono, et_postal, et_direccion, et_captcha;
    private static final int PICK_IMAGE = 1;
    private byte[] imagenBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        et_nombre = findViewById(R.id.et_nombrer);
        et_apellidos = findViewById(R.id.et_apellidosr);
        et_dni = findViewById(R.id.et_dnir);
        et_mail = findViewById(R.id.et_correor);
        et_contrasena = findViewById(R.id.et_contrasenar);
        et_concontrasena = findViewById(R.id.et_concontrasenar);
        et_nombreempresa = findViewById(R.id.et_nombreempresar);
        et_telefono = findViewById(R.id.et_telefonor);
        et_postal = findViewById(R.id.et_postalr);
        et_direccion = findViewById(R.id.et_direccionr);
        et_captcha = findViewById(R.id.et_captchar);
        Button but_logo = findViewById(R.id.but_logor);
        Button but_registro = findViewById(R.id.but_registror);

        but_logo.setOnClickListener(v -> seleccionarImagen());

        //Realizar el registro
        but_registro.setOnClickListener(v -> {
            try {
                String dni = et_dni.getText().toString();
                String contrasena = et_contrasena.getText().toString();
                String concontrasena = et_concontrasena.getText().toString();
                String captcha = et_captcha.getText().toString();

                try (DBUsuario dbUsuario = new DBUsuario(Registro.this)) {
                    if (dbUsuario.existeUsuarioConDNI(dni)) {
                        Toast.makeText(Registro.this, "El usuario con este DNI ya está registrado", Toast.LENGTH_SHORT).show();
                    } else if (!dniValido(dni)){
                        Toast.makeText(Registro.this, "El DNI no es válido", Toast.LENGTH_SHORT).show();
                    } else if (!contrasenaCorrecta(contrasena)){
                        Toast.makeText(Registro.this, "La contraseña tiene que tener al menos 3 caracteres incluyendo letras mayúsculas, minúsculas y números", Toast.LENGTH_SHORT).show();
                    } else if (!contrasena.equals(concontrasena)){
                        Toast.makeText(Registro.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    } else if (!captcha.equals("1234")){
                        Toast.makeText(Registro.this, "Captcha incorrecto", Toast.LENGTH_SHORT).show();
                    } else {
                        try (DBUsuario dbusuario = new DBUsuario(Registro.this)){
                            long id = dbusuario.insertaUsuario(et_nombre.getText().toString(),et_apellidos.getText().toString(),et_dni.getText().toString(),et_mail.getText().toString(),et_contrasena.getText().toString(),et_nombreempresa.getText().toString(),et_telefono.getText().toString(),et_postal.getText().toString(),et_direccion.getText().toString(), imagenBytes);

                            if (id > 0) {
                                Toast.makeText(Registro.this, "REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
                                limpiar();
                                Intent regresar = new Intent(Registro.this, MainActivity.class);
                                startActivity(regresar);
                            } else {
                                Toast.makeText(Registro.this, "ERROR AL REGISTRARSE", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Registro.this, "Error al insertar usuario: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Registro.this, "Error al abrir la base de datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Registro.this, "Se produjo un error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //Validar el dni
    private boolean dniValido(String dni) {
        if (dni.length() != 9 || !dni.matches("\\d{8}[A-Za-z]")) {
            return false;
        }

        int numeroDNI = Integer.parseInt(dni.substring(0, 8));
        char letraControl = dni.charAt(8);

        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        char letraEsperada = letras.charAt(numeroDNI % 23);

        return letraControl == letraEsperada;
    }

    //Validar la contraseña
    private boolean contrasenaCorrecta(String contrasena) {
        return contrasena.length() > 3 && contrasena.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    }
    //Seleccionar la imagen
    private void seleccionarImagen() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_IMAGE);
    }

    //Proceso para poder guardar la imagen del logo en la base de datos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imagenBytes = convertirBitmapABytes(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //Para guardarlo a la base de datos
    private byte[] convertirBitmapABytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    //Limpiar los plaintext
    private void limpiar(){
        et_nombre.setText("");
        et_apellidos.setText("");
        et_dni.setText("");
        et_mail.setText("");
        et_contrasena.setText("");
        et_concontrasena.setText("");
        et_nombreempresa.setText("");
        et_telefono.setText("");
        et_postal.setText("");
        et_direccion.setText("");
        et_captcha.setText("");

    }

}