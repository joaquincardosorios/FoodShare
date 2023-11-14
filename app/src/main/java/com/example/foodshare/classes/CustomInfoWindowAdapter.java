package com.example.foodshare.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodshare.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.time.format.DateTimeFormatter;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View windowView;
    Helper helper;
    private final LayoutInflater inflater;
    public CustomInfoWindowAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        windowView = inflater.inflate(R.layout.custom_info_window, null);
        helper = new Helper();
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {

        TextView titleTextView = windowView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = windowView.findViewById(R.id.descriptionTextView);
        TableLayout productsTableLayout = windowView.findViewById(R.id.productsTableLayout);

        Offer oferta = (Offer) marker.getTag();

        if (oferta != null) {
            titleTextView.setText(marker.getTitle());
            descriptionTextView.setText(oferta.getDescription());

            int numRows = productsTableLayout.getChildCount();
            int startIndex = 1;

            for (int i = startIndex; i < numRows; i++) {
                productsTableLayout.removeViewAt(startIndex);
            }

            for (Product producto : oferta.getProducts()) {
                TableRow row = new TableRow(windowView.getContext());
                row.addView(helper.crearTextView(windowView.getContext(),producto.getName(), 12,1,8));
                row.addView(helper.crearTextView(windowView.getContext(),producto.getQuantity(), 12,1,8));

                String date = producto.getExpirationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                row.addView(helper.crearTextView(windowView.getContext(),date, 12,1,8));

                productsTableLayout.addView(row);
            }
        }

        return windowView;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }
}
