package com.example.george.enrutamiento.Enrutador;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.george.enrutamiento.R;

public class view_ruta extends AppCompatActivity implements  View.OnClickListener, View.OnTouchListener
{
    private FloatingActionButton back;//flotate de atras
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ruta);
        back=findViewById(R.id.back);
        back.setOnClickListener(this);

            String url = Enrutador.ruta(this);
            WebView webView =findViewById(R.id.web_view);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient());
            webView.setClickable(false);
            webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {

                try
                {
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

        });
    }

    @Override
    public void onClick(View v)
    {
        int i=v.getId();
        switch (i)
        {
            case R.id.back:
                finish();
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        return false;
    }
}
