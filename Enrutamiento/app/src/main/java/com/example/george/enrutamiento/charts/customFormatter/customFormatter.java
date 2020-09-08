package com.example.george.enrutamiento.charts.customFormatter;

import com.github.mikephil.charting.charts.BarLineChartBase;

import java.util.Map;


/**
 * Created by Wiro on 09/08/19.
 */
public class customFormatter extends ValueFormatter
{

    Map<Integer,String> labels;

    private final BarLineChartBase<?> chart;

    public customFormatter(BarLineChartBase<?> chart, Map<Integer, String> labels) {
        this.chart = chart;
        this.labels=labels;
    }

    @Override
    public String getFormattedValue(float value) {
        return labels.get((int)value);
    }

}
