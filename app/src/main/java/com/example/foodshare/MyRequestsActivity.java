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
import android.widget.Toast;

import com.example.foodshare.R;
import com.example.foodshare.classes.Helper;
import com.example.foodshare.classes.Offer;
import com.example.foodshare.classes.Request;
import com.example.foodshare.classes.RequestAdapter;
import com.example.foodshare.classes.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyRequestsActivity extends AppCompatActivity {
    ListView lvMyRequests;
    Helper helper;
    User user;
    List<Request> requestList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        Toolbar toolbar = findViewById(R.id.toolbarMyRequest);
        setSupportActionBar(toolbar);
        Drawable overflowIcon = toolbar.getOverflowIcon();
        if (overflowIcon != null) {
            DrawableCompat.setTint(overflowIcon, ContextCompat.getColor(this, R.color.black));
        }

        helper = new Helper();
        user = helper.getCurrentUser(this);
        requestList = helper.getList(this, "solicitudes", Request.class);
        List<Request> filteredRequests = (requestList != null)
                ? requestList.stream().filter(request -> request.getUserId() == user.getId()).collect(Collectors.toList())
                : new ArrayList<>();


        RequestAdapter adapter = new RequestAdapter(this, filteredRequests);
        lvMyRequests = findViewById(R.id.lv_my_requests);
        lvMyRequests.setAdapter(adapter);

        adapter.setOnDeleteButtonClickListener(new RequestAdapter.OnDeleteButtonClickListener() {
            @Override
            public void onDeleteButtonClick(int position) {
                int idRequestToDelete = filteredRequests.get(position).getId();
                List<Request> listaFiltrada = new ArrayList<>();
                for (Request request : requestList) {
                    if (request.getId() != idRequestToDelete) {
                        listaFiltrada.add(request);
                    }
                }
                helper.saveList(MyRequestsActivity.this, listaFiltrada, "solicitudes");
                startActivity(new Intent(MyRequestsActivity.this, MyRequestsActivity.class));
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