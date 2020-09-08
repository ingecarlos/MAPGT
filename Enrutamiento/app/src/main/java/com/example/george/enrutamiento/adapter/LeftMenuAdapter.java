package com.example.george.enrutamiento.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.george.enrutamiento.R;

public class LeftMenuAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public LeftMenuAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.left_menu, itemname);
        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
    }
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();


        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.left_menu, parent,false);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.item);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtTitle.setText(itemname[position]);
        holder.imageView.setImageResource(imgid[position]);


        return convertView;

    }
   private static class ViewHolder {
        private TextView txtTitle;
        private ImageView imageView;
    }
}