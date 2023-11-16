package com.example.foodshare.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodshare.R;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class RequestAdapter extends ArrayAdapter<Request> {
    private RequestAdapter.OnDeleteButtonClickListener onDeleteButtonClickListener;
    public void setOnDeleteButtonClickListener(RequestAdapter.OnDeleteButtonClickListener listener) {
        this.onDeleteButtonClickListener = listener;
    }
    public RequestAdapter(Context context, List<Request> requests) {
        super(context, 0, requests);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_request, parent, false);
        }

        Request request = getItem(position);
        if (request != null) {
            Helper helper = new Helper();
            int OfferId = request.getOfferId();
            List<Offer> offersList = helper.getList(getContext(), "ofertas", Offer.class);
            List<User> userList = helper.getList(getContext(), "usuarios", User.class);
            Offer foundOffer = findOfferById(OfferId, offersList);

            TextView textUserName = convertView.findViewById(R.id.textUserName);
            TextView textDate = convertView.findViewById(R.id.textDate);
            TextView textDescription = convertView.findViewById(R.id.textDescription);
            Button btnDelete = convertView.findViewById(R.id.btnDelete);

            if (foundOffer != null) {
                int userId = foundOffer.getUserId();
                User foundUser = findUserById(userId, userList);
                if (foundUser != null) {

                    textUserName.setText("Ofertante: " + foundUser.getName());
                    textDate.setText("Fecha de solicitud: " + foundOffer.getCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))); // Ajusta según tu implementación
                    textDescription.setText("Descripción: " + foundOffer.getDescription());
                }
            }

            btnDelete.setOnClickListener(v -> {
                if (onDeleteButtonClickListener != null) {
                    onDeleteButtonClickListener.onDeleteButtonClick(position);
                }
            });
        }

        return convertView;
    }

    private Offer findOfferById(int idOffer, List<Offer> offersList) {
        for (Offer offer : offersList) {
            if (offer.getId() == idOffer) {
                return offer;
            }
        }
        return null;
    }

    private User findUserById(int userId, List<User> usuariosList) {
        for (User user : usuariosList) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }
}