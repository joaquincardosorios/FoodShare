package com.example.foodshare.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Helper {
    private static final String PREF_NAME = "Preferences";
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

    public <T> void saveList(Context context, List<T> list, String listName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        String listJson = gson.toJson(list);

        editor.putString(listName, listJson);
        editor.apply();
    }

    public <T> List<T> getList(Context context, String listName, Class<T> type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String listJson = sharedPreferences.getString(listName, null);

        if (listJson != null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();

            Type listType = TypeToken.getParameterized(List.class, type).getType();
            return gson.fromJson(listJson, listType);
        } else {
            return null;
        }
    }

    public User getCurrentUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("usuario", null);
        if(userJson != null){
            Gson gson = new Gson();
            User user = gson.fromJson(userJson, User.class);
            return user;
        } else {
            return null;
        }

    }

    public void saveLocalUser(Context context, User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString("usuario", userJson);
        editor.apply();
    }
}
