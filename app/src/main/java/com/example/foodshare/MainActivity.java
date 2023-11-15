package com.example.foodshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.foodshare.classes.CustomInfoWindowAdapter;
import com.example.foodshare.classes.Helper;
import com.example.foodshare.classes.Offer;
import com.example.foodshare.classes.Product;
import com.example.foodshare.classes.Request;
import com.example.foodshare.classes.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.example.foodshare.Data;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener
{
    Spinner spCiudades;
    SpinnerAdapter adapter;
    GoogleMap mMap;
    Button btnPedir, btnOfrecer;
    List<User> usuarios;
    List<Offer> ofertas;
    List<Request> solicitudes;
    Offer offerSelected;
    User user;
    Helper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new Helper();

        usuarios = helper.getList(this, "usuarios", User.class);
        if (usuarios == null) {
            usuarios = Data.getUsersList();
            helper.saveList(this, usuarios, "usuarios");
        }
        ofertas = helper.getList(this, "ofertas", Offer.class);
        if (ofertas == null) {
            ofertas = Data.getOffersList();
            helper.saveList(this, ofertas, "ofertas");
        }
        solicitudes = helper.getList(this, "solicitudes", Request.class);
        if (solicitudes == null) {
            solicitudes= Data.getRequestList();
            helper.saveList(this, solicitudes, "solicitudes");
        }
        user = helper.getCurrentUser(this);
        if (user == null){
            user = Data.getUser();
            helper.saveLocalUser(this, user);
        }


        offerSelected = null;

        spCiudades = findViewById(R.id.spinner);
        adapter = new SpinnerAdapter(MainActivity.this, getResources().getStringArray(R.array.ciudades));
        spCiudades.setAdapter(adapter);
        btnPedir = findViewById(R.id.btnPedir);
        btnOfrecer = findViewById(R.id.btnOfrecer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Drawable overflowIcon = toolbar.getOverflowIcon();
        if (overflowIcon != null) {
            DrawableCompat.setTint(overflowIcon, ContextCompat.getColor(this, R.color.black));
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btnOfrecer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OfferProductsActivity.class);
            startActivity(intent);
        });
        
        btnPedir.setOnClickListener(v -> {
            if(offerSelected != null){
                showDialogPedido(offerSelected);
            } else {
                Toast.makeText(this, "Selecciona una oferta antes de continuar", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(v -> {
            offerSelected = null;
        });
        mMap.setOnMarkerClickListener(this);
        LatLng Chile = new LatLng(-36.8331312135371, -73.0564574815846);
        CustomInfoWindowAdapter infoWindowAdapter = new CustomInfoWindowAdapter(this);
        this.mMap.setInfoWindowAdapter(infoWindowAdapter);


        for (Offer oferta : ofertas){
            final String[] userNameFinal = {""};
            Optional<User> owner = usuarios.stream().filter(usuario -> usuario.getId() == oferta.UserId ).findFirst();
            owner.ifPresent(user -> userNameFinal[0] = user.getName());
            Marker marker = mMap.addMarker(
                    new MarkerOptions()
                    .position(new LatLng(oferta.Lat, oferta.Long))
                    .title(userNameFinal[0]+ ": ")
                    .icon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier("foodmarker","drawable", getPackageName())))
            );
            marker.setTag(oferta);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Chile));
    }
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        offerSelected = (Offer) marker.getTag();
        return false;
    }

    private void showDialogPedido(Offer offer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar productos:");

        List<Product> productos = Arrays.asList(offer.getProducts());

        List<String> productosStr = new ArrayList<>();
        for (Product producto : productos) {
            String productoStr = producto.getName() + " - Cantidad: " + producto.getQuantity();
            productosStr.add(productoStr);
        }

        CharSequence[] productosArray = productosStr.toArray(new CharSequence[0]);

        boolean[] productosSeleccionados = new boolean[productosArray.length];

        builder.setMultiChoiceItems(productosArray, productosSeleccionados, (dialog, which, isChecked) -> {
            productosSeleccionados[which] = isChecked;
        });

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            List<Product> productosSeleccionadosList = new ArrayList<>();
            for (int i = 0; i < productosSeleccionados.length; i++) {
                if (productosSeleccionados[i]) {
                    productosSeleccionadosList.add(productos.get(i));
                }
            }
            if (!productosSeleccionadosList.isEmpty()) {
                Request request = new Request(offerSelected.getId(), user.getId(), productosSeleccionadosList);
                solicitudes.add(request);
                helper.saveList(this, solicitudes, "solicitudes");
                mostrarMensajeAceptacion();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Por favor, seleccione al menos un producto antes de continuar.", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {

        });

        builder.show();
    }

    private void mostrarMensajeAceptacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Productos seleccionados!");
        builder.setMessage("Espera la confirmación del ofertante \n" +
                "luego puedes ir  a buscar los alimentos");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}