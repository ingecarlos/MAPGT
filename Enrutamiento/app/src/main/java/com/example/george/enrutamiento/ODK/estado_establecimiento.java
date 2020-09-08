package com.example.george.enrutamiento.ODK;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.example.george.enrutamiento.R;

public class estado_establecimiento extends AppCompatActivity implements
        View.OnClickListener{


    private FloatingActionButton back;//flotate de atras

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_establecimiento);

        WebView myWebView = findViewById(R.id.webview);
        myWebView.loadUrl("http://estadistica.mineduc.gob.gt/fichaescolar/");

        back=findViewById(R.id.back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        int i=v.getId();

        switch (i)
        {
            case R.id.back:
                finish();
                break;
        }

    }
}
