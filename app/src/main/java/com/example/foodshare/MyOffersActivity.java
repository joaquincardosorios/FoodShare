package com.example.foodshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.foodshare.classes.Helper;
import com.example.foodshare.classes.Offer;
import com.example.foodshare.classes.OfferAdapter;
import com.example.foodshare.classes.Request;
import com.example.foodshare.classes.RequestAdapter;
import com.example.foodshare.classes.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyOffersActivity extends AppCompatActivity {
    ListView lvMyOffers;
    Helper helper;
    User user;
    List<Offer> offerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offers);

        Toolbar toolbar = findViewById(R.id.toolbarMyOffers);
        setSupportActionBar(toolbar);
        Drawable overflowIcon = toolbar.getOverflowIcon();
        if (overflowIcon != null) {
            DrawableCompat.setTint(overflowIcon, ContextCompat.getColor(this, R.color.black));
        }

        helper = new Helper();
        user = helper.getCurrentUser(this);
        offerList = helper.getList(this, "ofertas", Offer.class);
        List<Offer> filteredOffers = (offerList != null)
                ? offerList.stream().filter(offer -> offer.getUserId() == user.getId()).collect(Collectors.toList())
                : new ArrayList<>();


        OfferAdapter adapter = new OfferAdapter(this, filteredOffers);
        lvMyOffers = findViewById(R.id.lv_my_offers);
        lvMyOffers.setAdapter(adapter);
        adapter.setOnDeleteButtonClickListener(new OfferAdapter.OnDeleteButtonClickListener() {
            @Override
            public void onDeleteButtonClick(int position) {
                int idOfferToDelete = filteredOffers.get(position).getId();
                List<Offer> listaFiltrada = new ArrayList<>();
                for (Offer offer : offerList) {
                    if (offer.getId() != idOfferToDelete) {
                        listaFiltrada.add(offer);
                    }
                }
                List<Request> requestList = helper.getList(MyOffersActivity.this,"solicitudes", Request.class);
                List<Request> listaFiltradaReq = new ArrayList<>();
                for (Request request : requestList) {
                    if (request.getOfferId() != idOfferToDelete) {
                        listaFiltradaReq.add(request);
                    }
                }
                helper.saveList(MyOffersActivity.this, listaFiltrada, "ofertas");
                helper.saveList(MyOffersActivity.this, listaFiltradaReq, "solicitudes");
                startActivity(new Intent(MyOffersActivity.this, MyOffersActivity.class));
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