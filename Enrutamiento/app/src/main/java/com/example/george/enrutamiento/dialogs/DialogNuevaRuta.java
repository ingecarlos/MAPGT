package com.example.george.enrutamiento.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.george.enrutamiento.R;


public class DialogNuevaRuta extends AppCompatActivity {
    public static final int REQUEST_DIALOG = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_nueva_ruta);
        this.setFinishOnTouchOutside(false);

        Intent intent = getIntent();
        String mensaje = intent.getStringExtra("mensaje");

        if (mensaje != null) { // si no se envió mensaje
            TextView dialogtext = (TextView) findViewById(R.id.dialog_info_text);
            dialogtext.setText(mensaje);
        }


        final TextView subscribe = (TextView) findViewById(R.id.dialog_warning_ok);

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("buttonPressed", "nueva");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        final TextView cancelar = (TextView) findViewById(R.id.dialog_warning_cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("buttonPressed", "mantener");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
        //bloquea el botón de retroceso
    }
}
