package com.example.george.enrutamiento.informeGraficos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.george.enrutamiento.ODKData.ODKData;
import com.example.george.enrutamiento.R;
import com.example.george.enrutamiento.charts.cubicChart;
import com.example.george.enrutamiento.charts.pieChart;
import com.example.george.enrutamiento.charts.stackedChart;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

public class informeGraficos extends AppCompatActivity {

    /*banderas para saber si los graficos ya estan listos para mostrarse*/
    private boolean g1=false;
    private boolean g2=false;
    private boolean g3=false;
    private boolean g4=false;
    private boolean g5=false;

    private String nombre_fichero = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe_graficos);
        permissions();//necesarios para leer la memoria


        Intent intent = getIntent();
        File formulario = (File) intent.getExtras().get("formulario");

        TextView subTitulo = findViewById(R.id.subTituloID);
        subTitulo.setText((String) intent.getExtras().get("codigo") + "\n"
                + (String) intent.getExtras().get("grado") + "\n"
                + (String) intent.getExtras().get("asignatura") + "\n"
                + (String) intent.getExtras().get("fecha")

        );

        nombre_fichero = (String) intent.getExtras().get("codigo") + "_"
                + (String) intent.getExtras().get("grado") + "_"
                + (String) intent.getExtras().get("asignatura") + "_"
                + (String) intent.getExtras().get("fecha")
        ;

        //Toast.makeText(getApplicationContext(), formulario.toString(), Toast.LENGTH_SHORT).show();

        ODKData.formulario = formulario;
  /*      dibujarActividadesAcademicas();
        dibujarPromedioInvolucramiento();
        dibujarActividadesInvolucramiento();
        dibujarMateriales();
        dibujarMaterialesInvolucramiento();
*/


        //deshabiilitar el touch para que no haya error de que se toquen cosas graficas
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        /*progressBarr*/
        ((ProgressBar) findViewById(R.id.progressBar)).setIndeterminate(true);

        /*ocultar graficos*/
        findViewById(R.id.chartActividades).setVisibility(View.INVISIBLE);
        findViewById(R.id.chartPromedioInvolucrados).setVisibility(View.INVISIBLE);
        findViewById(R.id.porcentajeAlumnosLabel).setVisibility(View.INVISIBLE);
        findViewById(R.id.labelsGrafico3).setVisibility(View.INVISIBLE);
        findViewById(R.id.chartActividadesInvolucramiento).setVisibility(View.INVISIBLE);
        findViewById(R.id.chartMateriales).setVisibility(View.INVISIBLE);
        findViewById(R.id.chartMaterialesInvolucramiento).setVisibility(View.INVISIBLE);

        /*ocultar titulos*/

        findViewById(R.id.actividadesID).setVisibility(View.INVISIBLE);
        findViewById(R.id.promedioInvolucradosID).setVisibility(View.INVISIBLE);
        findViewById(R.id.actividadesInvolucramientoID).setVisibility(View.INVISIBLE);
        findViewById(R.id.materialesID).setVisibility(View.INVISIBLE);
        findViewById(R.id.materialesInvolucramientoID).setVisibility(View.INVISIBLE);


        new CountDownTimer(500, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                dibujar();
            }
        }.start();
    }

    @Override
    public void onBackPressed(){
        if (tGrafico1!=null) tGrafico1.interrupt();
        if (tGrafico2!=null) tGrafico2.interrupt();
        if (tGrafico3!=null) tGrafico3.interrupt();
        if (tGrafico4!=null) tGrafico4.interrupt();
        if (tGrafico5!=null) tGrafico5.interrupt();

        ODKData.resetLabels();
        super.onBackPressed();
    }

    public void setLabels5() {
        /*LABELS GRAFICO 5*/
        String labelGrafico4 = "";
        if (!ODKData.CUADERNOS_STR_LARGE.contains("$NO$")) {
            labelGrafico4 += ODKData.CUADERNOS_STR.substring(ODKData.CUADERNOS_STR.length() - 2) + ". " + ODKData.CUADERNOS_STR.substring(0, ODKData.CUADERNOS_STR.length() - 2) + " = " + ODKData.CUADERNOS_STR_LARGE + "\n";
        }
        if (!ODKData.DIDACTICO_STR_LARGE.contains("$NO$")) {
            labelGrafico4 += ODKData.DIDACTICO_STR.substring(ODKData.DIDACTICO_STR.length() - 2) + ". " + ODKData.DIDACTICO_STR.substring(0, ODKData.DIDACTICO_STR.length() - 2) + " = " + ODKData.DIDACTICO_STR_LARGE + "\n";
        }
        if (!ODKData.COOPERATIVA_STR_LARGE.contains("$NO$")) {
            labelGrafico4 += ODKData.COOPERATIVA_STR.substring(ODKData.COOPERATIVA_STR.length() - 2) + ". " + ODKData.COOPERATIVA_STR.substring(0, ODKData.COOPERATIVA_STR.length() - 2) + " = " + ODKData.COOPERATIVA_STR_LARGE + "\n";
        }

        ((TextView) findViewById(R.id.labelsGrafico4)).setText(labelGrafico4);

        ODKData.resetLabels();

    }

    public void setLabels3() {
        /*LABELS GRAFICO 3*/
        String labelGrafico3 = "";
        if (!ODKData.DEMOSTRANDO_STR_LARGE.contains("$NO$")) {
            labelGrafico3 += ODKData.DEMOSTRANDO_STR.substring(ODKData.DEMOSTRANDO_STR.length() - 2) + ". " + ODKData.DEMOSTRANDO_STR.substring(0, ODKData.DEMOSTRANDO_STR.length() - 2) + " = " + ODKData.DEMOSTRANDO_STR_LARGE + "\n";
        }
        if (!ODKData.PREGUNTAS_STR_LARGE.contains("$NO$")) {
            labelGrafico3 += ODKData.PREGUNTAS_STR.substring(ODKData.PREGUNTAS_STR.length() - 2) + ". " + ODKData.PREGUNTAS_STR.substring(0, ODKData.PREGUNTAS_STR.length() - 2) + " = " + ODKData.PREGUNTAS_STR_LARGE + "\n";
        }
        if (!ODKData.PRACTICANDO_STR_LARGE.contains("$NO$")) {
            labelGrafico3 += ODKData.PRACTICANDO_STR.substring(ODKData.PRACTICANDO_STR.length() - 2) + ". " + ODKData.PRACTICANDO_STR.substring(0, ODKData.PRACTICANDO_STR.length() - 2) + " = " + ODKData.PRACTICANDO_STR_LARGE + "\n";
        }
        if (!ODKData.ORGANIZACION_STR_LARGE.contains("$NO$")) {
            labelGrafico3 += ODKData.ORGANIZACION_STR.substring(ODKData.ORGANIZACION_STR.length() - 2) + ". " + ODKData.ORGANIZACION_STR.substring(0, ODKData.ORGANIZACION_STR.length() - 2) + " = " + ODKData.ORGANIZACION_STR_LARGE + "\n";
        }
        if (!ODKData.GESTION_STR_LARGE.contains("$NO$")) {
            labelGrafico3 += ODKData.GESTION_STR.substring(ODKData.GESTION_STR.length() - 2) + ". " + ODKData.GESTION_STR.substring(0, ODKData.GESTION_STR.length() - 2) + " = " + ODKData.GESTION_STR_LARGE + "\n";
        }
        if (!ODKData.SOCIALALUMNOS_STR_LARGE.contains("$NO$")) {
            labelGrafico3 += ODKData.SOCIALALUMNOS_STR.substring(ODKData.SOCIALALUMNOS_STR.length() - 2) + ". " + ODKData.SOCIALALUMNOS_STR.substring(0, ODKData.SOCIALALUMNOS_STR.length() - 2) + " = " + ODKData.SOCIALALUMNOS_STR_LARGE + "\n";
        }
        if (!ODKData.SOCUALEXTERNAS_STR_LARGE.contains("$NO$")) {
            labelGrafico3 += ODKData.SOCUALEXTERNAS_STR.substring(ODKData.SOCUALEXTERNAS_STR.length() - 2) + ". " + ODKData.SOCUALEXTERNAS_STR.substring(0, ODKData.SOCUALEXTERNAS_STR.length() - 2) + " = " + ODKData.SOCUALEXTERNAS_STR_LARGE + "\n";
        }
        if (!ODKData.FUERAAULA_STR_LARGE.contains("$NO$")) {
            labelGrafico3 += ODKData.FUERAAULA_STR.substring(ODKData.FUERAAULA_STR.length() - 2) + ". " + ODKData.FUERAAULA_STR.substring(0, ODKData.FUERAAULA_STR.length() - 2) + " = " + ODKData.FUERAAULA_STR_LARGE + "\n";
        }
        ((TextView) findViewById(R.id.labelsGrafico3)).setText(labelGrafico3);

        //ODKData.resetLabels();

    }

    public void dibujar() {

        dibujarActividadesAcademicas();
        dibujarPromedioInvolucramiento();
        dibujarActividadesInvolucramiento();
        dibujarMaterialesInvolucramiento();
        dibujarMateriales();
    }

    public void guardarReporte(View v) {

        Button reporte = findViewById(R.id.guardarReporteBtn);
        reporte.setVisibility(View.INVISIBLE);

        View view = findViewById(R.id.scrollView);//scrollView
        ScrollView sv = (ScrollView) view;
        int height = sv.getChildAt(0).getHeight();
        int width = sv.getChildAt(0).getWidth();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);


        //store bitmap
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ReportesODK";
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, nombre_fichero + ".png");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
            Toast.makeText(getApplicationContext(), "Reporte almacenado la carpeta local ReportesODK", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        reporte.setVisibility(View.VISIBLE);
    }


    public Bitmap catBitmap(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;
        int width, height = 0;
        if (c.getWidth() > s.getWidth()) {
            width = c.getWidth() + s.getWidth();
            height = c.getHeight();
        } else {
            width = s.getWidth() + s.getWidth();
            height = c.getHeight();
        }
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, c.getWidth(), 0f, null);
        return cs;
    }

    pieChart chartMateriales = new pieChart();
    Thread tGrafico5=null;
    private void dibujarMateriales() {
        chartMateriales = new pieChart();
        chartMateriales.iniChart(findViewById(R.id.chartMateriales));

        tGrafico5 = new Thread(new Runnable() {
            public void run() {
                ODKData.getDataSetMateriales();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chartMateriales.setData(ODKData.dataSetMateriales);
                        g4=true;
                        mostrarGraficos();
                    }
                });

            }
        });
        tGrafico5.start();
    }

    stackedChart chartMaterialesInvolucramiento = new stackedChart();
    Thread tGrafico4=null;
    private void dibujarMaterialesInvolucramiento() {
        chartMaterialesInvolucramiento = new stackedChart();

        tGrafico4 = new Thread(new Runnable() {
            public void run() {

                ODKData.getDataSetMaterialesInvolucramiento();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Map<Integer, String> labels = ODKData.getLabels(5);
                        chartMaterialesInvolucramiento.iniChart(findViewById(R.id.chartMaterialesInvolucramiento), labels);
                        chartMaterialesInvolucramiento.setData(ODKData.dataSetMaterialesInvolucramiento);
                        g5=true;
                        setLabels5();
                        mostrarGraficos();

                    }
                });

            }
        });
        tGrafico4.start();
    }

    stackedChart chartActividadesInvolucramiento = new stackedChart();
    Thread tGrafico3=null;
    private void dibujarActividadesInvolucramiento() {
        chartActividadesInvolucramiento = new stackedChart();


        tGrafico3 = new Thread(new Runnable() {
            public void run() {
                ODKData.getDataSetActividadesInvolucramiento();
                final Map<Integer, String> labels = ODKData.getLabels(3);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chartActividadesInvolucramiento.iniChart(findViewById(R.id.chartActividadesInvolucramiento), labels);
                        chartActividadesInvolucramiento.setData(ODKData.dataSetActividadesInvolucramiento);
                        g3=true;
                        setLabels3();
                        mostrarGraficos();
                    }
                });

            }
        });
        tGrafico3.start();
    }

    pieChart chartActividaes = new pieChart();
    Thread tGrafico1=null;
    private void dibujarActividadesAcademicas() {
        chartActividaes = new pieChart();
        chartActividaes.iniChart(findViewById(R.id.chartActividades));

        tGrafico1 = new Thread(new Runnable() {
            public void run() {
                    ODKData.getDataSetActividades();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            chartActividaes.setData(ODKData.dataSetActividades);
                            g1 = true;
                            mostrarGraficos();
                        }
                    });


            }
        });
        tGrafico1.start();
    }

    private void mostrarGraficos() {
        if (g1 && g2 && g3 && g4 && g5) {
            /*actividades academicas*/
            findViewById(R.id.chartActividades).setVisibility(View.VISIBLE);
            findViewById(R.id.chartActividades).invalidate();

            /*promedio involucramiento*/
            findViewById(R.id.chartPromedioInvolucrados).setVisibility(View.VISIBLE);
            findViewById(R.id.chartPromedioInvolucrados).invalidate();

            /*actividades involucramiento*/
            findViewById(R.id.chartActividadesInvolucramiento).setVisibility(View.VISIBLE);
            findViewById(R.id.chartActividadesInvolucramiento).invalidate();

            findViewById(R.id.porcentajeAlumnosLabel).setVisibility(View.VISIBLE);
            findViewById(R.id.labelsGrafico3).setVisibility(View.VISIBLE);
            findViewById(R.id.labelsGrafico3).invalidate();

            //setLabels3();

            /*materiales*/
            findViewById(R.id.chartMateriales).setVisibility(View.VISIBLE);
            findViewById(R.id.chartMateriales).invalidate();

            /*materiales involucramiento*/

            findViewById(R.id.chartMaterialesInvolucramiento).setVisibility(View.VISIBLE);
            findViewById(R.id.chartMaterialesInvolucramiento).invalidate();

            //setLabels5();

            /*mostrar titulos*/

            findViewById(R.id.actividadesID).setVisibility(View.VISIBLE);
            findViewById(R.id.promedioInvolucradosID).setVisibility(View.VISIBLE);
            findViewById(R.id.actividadesInvolucramientoID).setVisibility(View.VISIBLE);
            findViewById(R.id.materialesID).setVisibility(View.VISIBLE);
            findViewById(R.id.materialesInvolucramientoID).setVisibility(View.VISIBLE);

            /*ocultar progressbar*/
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            findViewById(R.id.tbObteniendoInforme).setVisibility(View.GONE);


            //desbloquear la pantalla
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    cubicChart chartPromedioInvolucramiento = new cubicChart();
    Thread tGrafico2=null;
    private void dibujarPromedioInvolucramiento() {
        chartPromedioInvolucramiento = new cubicChart();
        chartPromedioInvolucramiento.iniChart(findViewById(R.id.chartPromedioInvolucrados));

        tGrafico2 = new Thread(new Runnable() {
            public void run() {
                ODKData.getDataSetPromedioInvolucramiento();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chartPromedioInvolucramiento.setData(ODKData.dataSetPromedioInvolucramiento);
                        g2=true;
                        mostrarGraficos();
                    }
                });

            }
        });
        tGrafico2.start();
    }


    private void permissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (arePermissionsDenied()) {
                requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            }
        }
    }

    private static final int REQUEST_PERMISSIONS = 1234;
    private static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSIONS_COUNT = 2;

    private boolean arePermissionsDenied() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int p = 0;
            while (p < PERMISSIONS_COUNT) {
                if (checkSelfPermission(PERMISSIONS[p]) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
                p++;
            }
        }
        return false;
    }

}
