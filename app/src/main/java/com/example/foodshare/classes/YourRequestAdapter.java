package com.example.foodshare.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodshare.R;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class YourRequestAdapter extends ArrayAdapter<Request> {

    private YourRequestAdapter.OnNegateButtonClickListener onNegateButtonClickListener;
    public void setOnNegateButtonClickListener(YourRequestAdapter.OnNegateButtonClickListener listener) {
        this.onNegateButtonClickListener = listener;
    }
    private YourRequestAdapter.OnConfirmButtonClickListener onConfirmButtonClickListener;
    public void setOnConfirmButtonClickListener(YourRequestAdapter.OnConfirmButtonClickListener listener) {
        this.onConfirmButtonClickListener = listener;
    }
    public YourRequestAdapter(Context context, List<Request> requests) {
        super(context, 0, requests);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_your_request, parent, false);
        }

        Request request = getItem(position);
        if (request != null) {
            Helper helper = new Helper();
            List<User> userList = helper.getList(getContext(), "usuarios", User.class);

            TextView textUserName = convertView.findViewById(R.id.textYourSolicitante);
            TextView textDate = convertView.findViewById(R.id.textYourFechaSolicitud);
            LinearLayout linearProductos = convertView.findViewById(R.id.linearYourProductos);
            ImageButton btnNegate = convertView.findViewById(R.id.btnNegateRequest);
            ImageButton btnConfirm = convertView.findViewById(R.id.btnConfirmRequest);

            int userId = request.getUserId();
            User foundUser = findUserById(userId, userList);
            if (foundUser != null) {

                textUserName.setText("Solicitante: " + foundUser.getName());
                textDate.setText("Fecha de solicitud: " + request.getCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))); // Ajusta según tu implementación

                for (Product product : request.getProducts()) {
                    TextView textProductInfo = new TextView(getContext());
                    textProductInfo.setText(product.getName() + " : " + product.getQuantity());
                    linearProductos.addView(textProductInfo);
                }

            }


            btnNegate.setOnClickListener(v -> {
                if (onNegateButtonClickListener != null) {
                    onNegateButtonClickListener.onNegateButtonClick(position);
                }
            });

            btnConfirm.setOnClickListener(v -> {
                if (onConfirmButtonClickListener != null) {
                    onConfirmButtonClickListener.onConfirmButtonClick(position);
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

    public interface OnNegateButtonClickListener {
        void onNegateButtonClick(int position);
    }

    public interface OnConfirmButtonClickListener {
        void onConfirmButtonClick(int position);
    }
}
