package com.example.george.enrutamiento.charts;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.example.george.enrutamiento.charts.customFormatter.PercentFormatterInt;
import com.example.george.enrutamiento.charts.customFormatter.customFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.PercentFormatter;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class stackedChart implements OnChartValueSelectedListener {


    private HorizontalBarChart chart;

    public void iniChart(View ChartView,Map<Integer,String> labels){
        chart = (HorizontalBarChart) ChartView;

        //chart.setOnChartValueSelectedListener(this);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(40);



        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);

        chart.setDrawValueAboveBar(false);
        chart.setHighlightFullBarEnabled(false);


        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleEnabled(false);

        // change the position of the y-labels
        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setValueFormatter(new MyValueFormatter("K"));
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(new PercentFormatterInt());
        leftAxis.setTextSize(11f);


        chart.getAxisRight().setEnabled(false);



        XAxis xLabels = chart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setGranularity(1);
        xLabels.setValueFormatter( new customFormatter(chart,labels));
        xLabels.setTextSize(11f);
        //xLabels.setLabelRotationAngle(-90);



        Legend l = chart.getLegend();

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(10f);
        l.setFormToTextSpace(10f);
        l.setXEntrySpace(10f);
        l.setTextSize(12f);





    }

    public void setData(Map<Integer,float[]> dataSet){
        ArrayList<BarEntry> values = new ArrayList<>();

        int i = 5;

        Map<Integer,float[]> sortedDataSet = new TreeMap<Integer,float[]>(dataSet);

        for (Map.Entry<Integer,float[]> par:sortedDataSet.entrySet()) {
            i=(int)par.getKey();
            float val1 = par.getValue()[0];
            float val2 = par.getValue()[1];


            values.add(new BarEntry(
                    i,
                    new float[]{val1, val2}));


        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "");
            set1.setDrawIcons(false);
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"Involucrados", "No Involucrados"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new PercentFormatterInt());
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(11f);
            chart.setData(data);
        }

        chart.setFitBars(true);
        chart.invalidate();
    }

    private int[] getColors() {

        // have as many colors as stack-values per entry
        int[] colors = new int[2];

        System.arraycopy(ColorTemplate.VORDIPLOM_COLORS, 0, colors, 0, 2);

        return colors;
/*
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());


        int[] col = new int[colors.size()];
        for(int i=0;i<colors.size();i++)
            col[i]=colors.get(i);
        return col;
        */
    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        BarEntry entry = (BarEntry) e;

        if (entry.getYVals() != null)
            Log.i("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);
        else
            Log.i("VAL SELECTED", "Value: " + entry.getY());
    }

    @Override
    public void onNothingSelected() {}


}
