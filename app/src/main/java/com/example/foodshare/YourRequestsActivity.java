package com.example.foodshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.foodshare.classes.Helper;
import com.example.foodshare.classes.Offer;
import com.example.foodshare.classes.Request;
import com.example.foodshare.classes.RequestAdapter;
import com.example.foodshare.classes.User;
import com.example.foodshare.classes.YourRequestAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class YourRequestsActivity extends AppCompatActivity {
    ListView lvMyRequests;
    Helper helper;
    User user;
    List<Request> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_requests);

        Toolbar toolbar = findViewById(R.id.toolbarYourRequest);
        setSupportActionBar(toolbar);
        Drawable overflowIcon = toolbar.getOverflowIcon();
        if (overflowIcon != null) {
            DrawableCompat.setTint(overflowIcon, ContextCompat.getColor(this, R.color.black));
        }

        helper = new Helper();
        user = helper.getCurrentUser(this);
        List<Offer> offerList = helper.getList(this, "ofertas", Offer.class);
        requestList = helper.getList(this, "solicitudes", Request.class);
        List<Request> filteredRequests = new ArrayList<>();
        if( requestList != null && offerList != null){
            for (Request request : requestList){
                int offerId = request.getOfferId();
                for(Offer offer : offerList){
                    if(offerId == offer.getId() && offer.getUserId() == user.getId()){
                        filteredRequests.add(request);
                    }
                }
            }
        }

        YourRequestAdapter adapter = new YourRequestAdapter(this, filteredRequests);
        lvMyRequests = findViewById(R.id.lv_your_requests);
        lvMyRequests.setAdapter(adapter);

        adapter.setOnNegateButtonClickListener(new YourRequestAdapter.OnNegateButtonClickListener() {
            @Override
            public void onNegateButtonClick(int position) {
                int idRequestToDelete = filteredRequests.get(position).getId();
                List<Request> listaFiltrada = new ArrayList<>();
                for (Request request : requestList) {
                    if (request.getId() != idRequestToDelete) {
                        listaFiltrada.add(request);
                    }
                }
                helper.saveList(YourRequestsActivity.this, listaFiltrada, "solicitudes");
                startActivity(new Intent(YourRequestsActivity.this, YourRequestsActivity.class));

            }
        });

        adapter.setOnConfirmButtonClickListener(new YourRequestAdapter.OnConfirmButtonClickListener() {
            @Override
            public void onConfirmButtonClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(YourRequestsActivity.this);
                builder.setTitle("Solicitud Aceptada");
                builder.setMessage("Se habilitará un sistema interno de chat para que puedas comunicarte con la otra parte de manera segura.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        int idRequestToDelete = filteredRequests.get(position).getId();
                        List<Request> listaFiltrada = new ArrayList<>();
                        for (Request request : requestList) {
                            if (request.getId() != idRequestToDelete) {
                                listaFiltrada.add(request);
                            }
                        }
                        helper.saveList(YourRequestsActivity.this, listaFiltrada, "solicitudes");
                        startActivity(new Intent(YourRequestsActivity.this, YourRequestsActivity.class));
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_principal){
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if(id == R.id.action_offers){
            startActivity(new Intent(this, MyOffersActivity.class));
            return true;
        } else if (id == R.id.action_requests) {
            startActivity(new Intent(this, MyRequestsActivity.class));
            return true;
        } else if (id == R.id.action_your_requests) {
            startActivity(new Intent(this, YourRequestsActivity.class));
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }

    }
}