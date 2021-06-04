package com.example.namtcshop.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.namtcshop.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ThongTinActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Toolbar toolbar;
    private GoogleMap mgMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
        toolbar=(Toolbar)findViewById(R.id.toolbarThongTin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgMap = googleMap;
        // lay vi tri tren google map
        LatLng sydneys = new LatLng(20.97539429747738, 105.7811880550893);
        mgMap.addMarker(new MarkerOptions().position(sydneys).title("NamTCShop cửa hàng thiết bị di động").snippet("16 Trần Phú, Hà Đông, Hà Nội"));
        mgMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydneys).zoom(15).build();
        mgMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
