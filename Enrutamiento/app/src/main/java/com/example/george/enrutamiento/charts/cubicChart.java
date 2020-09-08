package com.example.george.enrutamiento.charts;

import android.graphics.Color;
import android.view.View;

import com.example.george.enrutamiento.charts.customFormatter.PercentFormatterInt;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class cubicChart {

    private LineChart chart;

    public void iniChart(View ChartView) {
        chart = (LineChart) ChartView;
        //chart.setViewPortOffsets(0, 0, 0, 0);
        //chart.setBackgroundColor(ColorTemplate.VORDIPLOM_COLORS[2]);

        // no description text
        //chart.getDescription().setEnabled(false);
        chart.getDescription().setText("Inicio                Desarrollo                Cierre ");
        chart.getDescription().setTextSize(13f);
        //chart.getDescription().

        // enable touch gestures
        chart.setTouchEnabled(false);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        //chart.setDrawGridBackground(false);
        chart.setMaxHighlightDistance(300);

        XAxis x = chart.getXAxis();
        //x.setEnabled(false);
        x.setLabelCount(12, false);
        x.setTextColor(Color.BLACK);
        //x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        x.setAxisLineColor(Color.BLACK);
        x.setGranularity(1);




        YAxis y = chart.getAxisLeft();
        y.setTextColor(Color.BLACK);
        //y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setAxisLineColor(Color.BLACK);
        y.setDrawZeroLine(false);
        y.setValueFormatter(new PercentFormatterInt());
        /*y.setGranularity(1);
        y.setLabelCount(9);
*/

        {   // // Create Limit Lines // //
            LimitLine llXAxis = new LimitLine(2f, "");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            //llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            //llXAxis.setTextSize(10f);
            //llXAxis.setTypeface(tfRegular);

            LimitLine llXAxis2 = new LimitLine(8f, "");
            llXAxis2.setLineWidth(4f);
            llXAxis2.enableDashedLine(10f, 10f, 0f);
            //llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            //llXAxis.setTextSize(10f);


            // draw limit lines behind data instead of on top
            y.setDrawLimitLinesBehindData(true);
            x.setDrawLimitLinesBehindData(true);

            // add limit lines
            x.addLimitLine(llXAxis);
            x.addLimitLine(llXAxis2);
            //xAxis.addLimitLine(llXAxis);
        }



        chart.getAxisRight().setEnabled(false);


        chart.getLegend().setEnabled(false);

        chart.animateXY(2000, 2000);



        // don't forget to refresh the drawing
        chart.invalidate();
    }


    public void setData(Map<Integer,Integer> dataSet) {

        Map<Integer,Integer> sortedDataSet = new TreeMap<Integer,Integer>(dataSet);

        ArrayList<Entry> values = new ArrayList<>();

        values.add(new Entry(0,0));

        for (Map.Entry<Integer,Integer> par:sortedDataSet.entrySet()) {
            values.add(new Entry(par.getKey(),par.getValue()));
        }

        //values.add(new Entry(11,0));


        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");


            //set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            //set1.setCubicIntensity(0.002f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.BLACK);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
            set1.setFillColor(ColorTemplate.VORDIPLOM_COLORS[2]);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // create a data object with the data sets
            LineData data = new LineData(set1);
            //data.setValueTypeface(tfLight);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            chart.setData(data);
        }
    }

}
