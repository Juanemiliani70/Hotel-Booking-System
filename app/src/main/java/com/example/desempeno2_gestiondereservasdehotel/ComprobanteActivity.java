package com.example.desempeno2_gestiondereservasdehotel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

public class ComprobanteActivity extends Activity {
    TextView tv2, tv3, tv4, tv5, tv6, tv7;
    Button btnVolver;

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_comprobante);

        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);
        btnVolver = findViewById(R.id.btn1);


        String nombre = getIntent().getStringExtra("nombre");
        String dni = getIntent().getStringExtra("dni");
        String email = getIntent().getStringExtra("email");
        String tipo = getIntent().getStringExtra("tipo_habitacion");
        double total = getIntent().getDoubleExtra("total", 0.0);


        tv2.setText("Nombre: " + nombre);
        tv3.setText("DNI: " + dni);
        tv4.setText("Email: " + email);
        tv5.setText("Tipo de habitación: " + tipo);
        tv6.setText("Total a pagar: $ " + String.format("%.2f", total));
        tv7.setText("Desarrollado por Juan Manuel Emiliani");



        btnVolver.setOnClickListener(v -> finish());
    }
}
