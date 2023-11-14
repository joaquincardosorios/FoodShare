package com.example.foodshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.foodshare.classes.CustomInfoWindowAdapter;
import com.example.foodshare.classes.Offer;
import com.example.foodshare.classes.Product;
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

    Offer offerSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuarios = Data.getUsersList();
        Intent i = getIntent();
        ofertas =(List<Offer>) i.getSerializableExtra("ofertas");
        if (ofertas == null){
            ofertas = Data.getOffersList();
        }
        offerSelected = null;


        spCiudades = findViewById(R.id.spinner);
        adapter = new SpinnerAdapter(MainActivity.this, getResources().getStringArray(R.array.ciudades));
        spCiudades.setAdapter(adapter);
        btnPedir = findViewById(R.id.btnPedir);
        btnOfrecer = findViewById(R.id.btnOfrecer);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btnOfrecer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OfferProductsActivity.class);
            intent.putExtra("ofertas", (Serializable) ofertas);
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

        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {

        });

        builder.show();
    }
}