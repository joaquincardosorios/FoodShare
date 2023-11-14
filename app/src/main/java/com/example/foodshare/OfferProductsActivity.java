package com.example.foodshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.foodshare.classes.DatePickerFragment;
import com.example.foodshare.classes.Helper;
import com.example.foodshare.classes.Offer;
import com.example.foodshare.classes.Product;
import com.example.foodshare.classes.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OfferProductsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener
{

    EditText etNombre, etCantidad, etFecha, etDescription;
    Button btnAddProduct, btnOffer;
    ScrollView scrollProductos;
    TableLayout tlProductList;
    GoogleMap mMap;
    User user;
    Offer offer;
    List<Product> productos;
    String nombre, cantidad, fecha;
    Double lat, along;
    List<Offer> ofertas;
    Helper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_products);
        user = Data.getUser();
        etNombre = findViewById(R.id.et_nombre_alimento);
        etCantidad = findViewById(R.id.et_cantidad_alimento);
        etFecha = findViewById(R.id.et_fecha_vencimiento);
        etDescription = findViewById(R.id.etDescription);
        tlProductList = findViewById(R.id.tl_productos);
        btnAddProduct = findViewById(R.id.btn_agregar_producto);
        btnOffer = findViewById(R.id.btn_ofrecer);
        scrollProductos = findViewById(R.id.scroll_productos);

        helper = new Helper();
        Intent i = getIntent();
        ofertas =(List<Offer>) i.getSerializableExtra("ofertas");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapOffer);
        mapFragment.getMapAsync(this);

        offer = new Offer();
        offer.setUserId(user.getId());
        offer.setCreationDate(LocalDate.now());

        productos = new ArrayList<>();

        etFecha.setOnClickListener(view -> {
            showDatePickerDialog((EditText) view);
        });
        btnAddProduct.setOnClickListener(view -> {
            addProduct();
        });

        btnOffer.setOnClickListener(view -> {
            addOffer();
        });
    }

    private void addOffer() {
        if(validateOffer()){
            Product[] productArray  = new Product[productos.size()];
            productos.toArray(productArray);
            offer.setProducts(productArray);
            offer.setLat(lat);
            offer.setLong(along);
            offer.setDescription(etDescription.getText().toString());
            ofertas.add(offer);
            Intent i = new Intent(OfferProductsActivity.this, MainActivity.class);
            i.putExtra("ofertas", (Serializable) ofertas);
            startActivity(i);
        }
    }

    private void addProduct() {
        nombre = etNombre.getText().toString();
        cantidad = etCantidad.getText().toString();
        fecha = etFecha.getText().toString();

        if(validateTexts()){

            LocalDate newDate = helper.formatearFecha(fecha);

            Product producto = new Product(nombre,cantidad,newDate);
            productos.add(producto);
            etNombre.setText("");
            etFecha.setText("");
            etCantidad.setText("");
            scrollProductos.setVisibility(View.VISIBLE);
            actualizarTablaProductos();
        }
    }

    private boolean validateTexts() {
        Boolean validate = true;

        if(TextUtils.isEmpty(nombre)){
            etNombre.setError("Nombre no puede ir vacio");
            etNombre.requestFocus();
            validate = false;
        }
        if(TextUtils.isEmpty(cantidad)){
            etCantidad.setError("Cantidad no puede ir vacia");
            etCantidad.requestFocus();
            validate = false;
        }
        if(TextUtils.isEmpty(fecha)){
            etFecha.setError("Fecha no puede ir vacia");
            etFecha.requestFocus();
            validate = false;
        } else if(helper.formatearFecha(fecha).isBefore(LocalDate.now())){
            etFecha.setError("Fecha no puede ser anterior a hoy");
            etFecha.requestFocus();
            validate = false;
        }

        return validate;

    }

    private boolean validateOffer(){
        boolean validate = true;
        String description = etDescription.getText().toString();
        if(TextUtils.isEmpty(description)){
            etDescription.setError("Agrega una descripción de los productos");
            etDescription.requestFocus();
            validate = false;
        }
        if (productos.size() == 0){
            validate = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Aviso")
                    .setMessage("Agregar al menos un alimento.")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
        if(lat == null || along == null){
            validate = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Aviso")
                    .setMessage("Por favor, selecciona un punto en el mapa.")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
        return validate;
    }

    public void showDatePickerDialog(EditText v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(v);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void actualizarTablaProductos() {
        int numRows = tlProductList.getChildCount();
        int startIndex = 1;

        for (int i = startIndex; i < numRows; i++) {
            tlProductList.removeViewAt(startIndex);
        }
        for (Product producto : productos) {
            TableRow row = new TableRow(this);
            row.addView(crearTextView(producto.getName(), 20,1,8));
            row.addView(crearTextView(producto.getQuantity(), 20,1,8));

            String date = producto.getExpirationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            row.addView(crearTextView(date, 20,1,8));

            TextView textView = new TextView(this);
            textView.setText("X");
            textView.setTextSize(15);
            textView.setBackgroundResource(R.drawable.fondobtn);
            textView.setGravity(Gravity.CENTER);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
            textView.setLayoutParams(params);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = tlProductList.indexOfChild(row);
                    if (position >= 1 && position <= productos.size()) {
                        productos.remove(position-1);
                        tlProductList.removeView(row);
                    }
                }
            });


            row.addView(textView);

            tlProductList.addView(row);
        }
    }

    private TextView crearTextView(String texto, int textSize, int layoutWeight, int padding) {
        TextView textView = new TextView(this);
        textView.setText(texto);
        textView.setTextSize(textSize);
        textView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, layoutWeight));
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        lat = latLng.latitude;
        along = latLng.longitude;
        mMap.clear();
        LatLng pos = new LatLng(lat, along);
        mMap.addMarker(
                new MarkerOptions()
                        .position(pos)
                        .title("Mi ubicación")
        );

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
        LatLng Chile = new LatLng(-36.8331312135371, -73.0564574815846);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(Chile));
    }
}