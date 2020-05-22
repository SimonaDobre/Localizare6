package com.simona.localizare6;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText edLati, edLongi, edOrasul;
    Button b;

    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        client = LocationServices.getFusedLocationProviderClient(this);
        initiali();
        actiuneButon();

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getObtineLocatiaCurenta();
        }
        else {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }




    }

    private void getObtineLocatiaCurenta(){
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null){
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latiLongi = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions optiuni = new MarkerOptions().position(latiLongi)
                                    .title("ESTI AICIII");
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latiLongi, 10));
                            mMap.addMarker(optiuni);

                        }
                    });
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

//        LatLng buch = new LatLng(44.423656, 26.163001);
//        mMap.addMarker(new MarkerOptions().position(buch).title("AICI BUCH"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(buch, 5));
//    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)  {
//        return;
//    }
//
//        mMap.setMyLocationEnabled(true);
   // boolean cumELocatia = mMap.setMyLocationEnabled(true);
       // Log.i("LOCATIA ", )

    }




    private void actiuneButon(){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edOrasul.getText().toString().equals("")){
                    mergiLaOrasul();
                }
                else if (!(edLati.getText().toString().equals("") && edLongi.getText().toString().equals(""))){
                    mergiLaLocatieCULatiSiLongi();
                }

                edOrasul.setText("");
                ascundeTastatura();

            }
        });
    }

    private void mergiLaOrasul(){
        String undeMerg = edOrasul.getText().toString();
        Geocoder gc = new Geocoder(this);
        try {
            List<Address> sirul = gc.getFromLocationName(undeMerg, 100);
            Address adresaUndeMerg = sirul.get(0);
          //  Toast.makeText(MapsActivity.this, "mergi la " + edOrasul.getText().toString(), Toast.LENGTH_SHORT).show();
            double lati = adresaUndeMerg.getLatitude();
            double longi = adresaUndeMerg.getLongitude();
            gotoMergiLaLocatieZoom(lati, longi, 3);
            LatLng orasNou  = new LatLng(lati, longi);
//            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(orasNou).title("AICI " + edOrasul.getText().toString()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(orasNou, 5));
            edOrasul.setText(null);
            edOrasul.setFocusable(true);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void mergiLaLocatieCULatiSiLongi(){
        double latitudineaDorita = Double.parseDouble(edLati.getText().toString());
        double longiDorita = Double.parseDouble(edLongi.getText().toString());
        gotoMergiLaLocatieZoom(latitudineaDorita, longiDorita, 7);
        Toast.makeText(MapsActivity.this, edLati.getText().toString(), Toast.LENGTH_SHORT).show();
        LatLng orasNou  = new LatLng(latitudineaDorita, longiDorita);
        mMap.addMarker(new MarkerOptions().position(orasNou).title("AICI " + edOrasul.getText().toString()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(orasNou, 5));
        edLati.setText(""); edLongi.setText("");
        edLati.setFocusable(true);
    }

    private void gotoMergiLaLocatieZoom(double lati, double longi, float zoom){
        LatLng lll = new LatLng(lati, longi);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lll, zoom));
    }


    private void initiali(){
        b = findViewById(R.id.button);
        edLati = findViewById(R.id.latiID);
        edLongi = findViewById(R.id.longiID);
        edOrasul = findViewById(R.id.editText);
    }

    private void ascundeTastatura(){
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edOrasul.getWindowToken(), 0);
    }


}
