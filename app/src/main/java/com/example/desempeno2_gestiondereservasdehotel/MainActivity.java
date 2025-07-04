package com.example.desempeno2_gestiondereservasdehotel;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    TextView txtview1, txtview2, textViewDatos, textViewNoches, textViewImporte;
    EditText editTextNombre, editTextEmail, editTextDNI, editTextNoches;
    RadioGroup radioGrup;
    RadioButton radiobtn1, radiobtn2, radiobtn3;
    Button btnCalcular;
    ImageButton btnRegistrar, btnLimpiar, btnVer, btnBuscar;
    double totalCalculado = 0.0;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        txtview1 = findViewById(R.id.txtview1);
        txtview2 = findViewById(R.id.txtview2);
        textViewDatos = findViewById(R.id.textViewDatos);
        textViewNoches = findViewById(R.id.textViewNoches);
        textViewImporte = findViewById(R.id.textViewImporte);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextDNI = findViewById(R.id.editTextDNI);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextNoches = findViewById(R.id.editTextNoches);

        radioGrup = findViewById(R.id.radioGrup);

        radiobtn1 = findViewById(R.id.radiobtn1);
        radiobtn2 = findViewById(R.id.radiobtn2);
        radiobtn3 = findViewById(R.id.radiobtn3);

        btnCalcular = findViewById(R.id.btnCalcular);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        btnVer = findViewById(R.id.btnVer);
        btnBuscar = findViewById(R.id.btnBuscar);
    }

    public void calcularImporte(View v) {
        String nochesStr = editTextNoches.getText().toString().trim();

        if (nochesStr.isEmpty()) {
            Utils.showCustomToast(this, "Ingrese un DNI");

            return;
        }

        int noches = Integer.parseInt(nochesStr);
        int seleccion = radioGrup.getCheckedRadioButtonId();

        if (seleccion == -1) {
            Utils.showCustomToast(this, "Seleccione una habitacion");
            return;
        }

        int precioPorNoche = 0;
        String tipo = "";

        if (seleccion == R.id.radiobtn1) {
            precioPorNoche = 10000;
            tipo = "Economica";
        } else if (seleccion == R.id.radiobtn2) {
            precioPorNoche = 18000;
            tipo = "Comoda";
        } else if (seleccion == R.id.radiobtn3) {
            precioPorNoche = 25000;
            tipo = "De lujo";
        }

        totalCalculado = noches * precioPorNoche;
        textViewImporte.setText("$ " + String.format("%.2f", totalCalculado));
    }

        public void registrarReserva(View v) {
        String nombre = editTextNombre.getText().toString().trim();
        String dni = editTextDNI.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String nochesStr = editTextNoches.getText().toString().trim();

        if (nombre.isEmpty() || dni.isEmpty() || email.isEmpty() || nochesStr.isEmpty() ||
                radioGrup.getCheckedRadioButtonId() == -1) {
            Utils.showCustomToast(this, "Debe completar todos los campos");
            return;
        }

        if (totalCalculado == 0) {
            Utils.showCustomToast(this, "Calcule el importe");
            return;

        }

        int noches = Integer.parseInt(nochesStr);
        RadioButton habitacionSeleccionada = findViewById(radioGrup.getCheckedRadioButtonId());
        String tipo = habitacionSeleccionada.getText().toString();

        BaseDeDatos admin = new BaseDeDatos(this, "reservas", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        db.execSQL("INSERT INTO reservas (nombre, dni, email, tipo_habitacion, noches, total) " +
                "VALUES ('" + nombre + "', '" + dni + "', '" + email + "', '" + tipo + "', " + noches + ", " + totalCalculado + ")");

        db.close();

      Utils.showCustomToast(this, "Reserva registrada");
    }

    public void buscarPorDni(View v) {
        String dni = editTextDNI.getText().toString().trim();

        if (dni.isEmpty()) {
           Utils.showCustomToast(this, "Ingrese un DNI");
        }

        BaseDeDatos dbHelper = new BaseDeDatos(MainActivity.this, "reservas", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT nombre, email, tipo_habitacion, noches, total FROM reservas WHERE dni = ?",
                new String[]{dni}
        );

        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(0);
            String email = cursor.getString(1);
            String tipo = cursor.getString(2);
            int noches = cursor.getInt(3);
            double total = cursor.getDouble(4);

            textViewDatos.setText(
                    "Nombre: " + nombre + "\n" +
                            "Email: " + email + "\n" +
                            "Habitacion: " + tipo + "\n" +
                            "Noches: " + noches + "\n" +
                            "Total: $ " + String.format("%.2f", total)
            );


            Utils.showCustomToast(this, "Reserva encontrada");
        } else {
            Utils.showCustomToast(this, "No se encontro una reserva con ese DNI");
            textViewDatos.setText("");
        }


        cursor.close();
        db.close();
    }

    public void verComprobante(View v) {
        BaseDeDatos dbHelper = new BaseDeDatos(MainActivity.this, "reservas", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT nombre, dni, email, tipo_habitacion, total FROM reservas",
                null
        );

        if (cursor.moveToLast()) {
            String nombre = cursor.getString(0);
            String dni = cursor.getString(1);
            String email = cursor.getString(2);
            String tipo = cursor.getString(3);
            double total = cursor.getDouble(4);

            Intent intent = new Intent(MainActivity.this, ComprobanteActivity.class);
            intent.putExtra("nombre", nombre);
            intent.putExtra("dni", dni);
            intent.putExtra("email", email);
            intent.putExtra("tipo_habitacion", tipo);
            intent.putExtra("total", total);
            startActivity(intent);
        } else {
            Utils.showCustomToast(this, "Debe registrar reserva para ver comprobante");

        }

        cursor.close();
        db.close();
    }


    public void limpiarCampos(View v) {
        editTextNombre.setText("");
        editTextDNI.setText("");
        editTextEmail.setText("");
        editTextNoches.setText("");
        radioGrup.clearCheck();
        textViewImporte.setText("");

        Utils.showCustomToast(this, "Se limpiaron los campos");


    }
}










