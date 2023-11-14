package com.example.foodshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private Context context;
    private String[] listaCiudades;

    public SpinnerAdapter(Context context, String[] listaCiudades){
        this.context = context;
        this.listaCiudades = listaCiudades;
    }
    @Override
    public int getCount() {
        return listaCiudades.length;
    }

    @Override
    public Object getItem(int i) {
        return listaCiudades[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_spinner, viewGroup,false);
        TextView txtName = rootView.findViewById(R.id.text);

        txtName.setText(listaCiudades[i]);
        return rootView;
    }
}
