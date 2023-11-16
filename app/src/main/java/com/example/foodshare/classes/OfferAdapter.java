package com.example.foodshare.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodshare.R;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OfferAdapter extends ArrayAdapter<Offer> {
    private OnDeleteButtonClickListener onDeleteButtonClickListener;
    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        this.onDeleteButtonClickListener = listener;
    }

    public OfferAdapter(Context context, List<Offer> offers) {
        super(context, 0, offers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_offer, parent, false);
        }

        Offer offer = getItem(position);
        if (offer != null) {
            Helper helper = new Helper();

            TextView textUserName = convertView.findViewById(R.id.textSolicitante);
            TextView textDate = convertView.findViewById(R.id.textFechaSolicitud);
            LinearLayout linearProductos = convertView.findViewById(R.id.linearProductos);
            Button btnDelete = convertView.findViewById(R.id.btnDeleteOffer);

            textUserName.setText("Fecha de creación: " + offer.getCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            textDate.setText("Descripción: " + offer.getDescription());

            List<Product> productList = Arrays.asList(offer.getProducts());

            for (Product product : productList) {
                TextView textProductInfo = new TextView(getContext());
                textProductInfo.setText(product.getName() + " : " + product.getQuantity());
                linearProductos.addView(textProductInfo);
            }
            btnDelete.setOnClickListener(v -> {
                if (onDeleteButtonClickListener != null) {
                    onDeleteButtonClickListener.onDeleteButtonClick(position);
                }
            });
        }


        return convertView;
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }
}
