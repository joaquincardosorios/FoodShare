package com.example.foodshare.classes;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Helper {
    public static LocalDate formatearFecha(String dateStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate newDate = LocalDate.parse(dateStr, formatter);
        return newDate;
    }

    public TextView crearTextView(Context context, String texto, int textSize, int layoutWeight, int padding) {
        TextView textView = new TextView(context);
        textView.setText(texto);
        textView.setTextSize(textSize);
        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, layoutWeight));
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }
}
