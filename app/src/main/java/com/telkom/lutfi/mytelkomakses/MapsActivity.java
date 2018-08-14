package com.telkom.lutfi.mytelkomakses;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.telkom.lutfi.mytelkomakses.model.User;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public Double longitude, latitude;



    private FirebaseFirestore mFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFireStore = FirebaseFirestore.getInstance();
        Query query = mFireStore.collection("user");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                mMap.clear();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    User tp = doc.toObject(User.class);
                   latitude = tp.getLatitude();
                    longitude = tp.getLonglitude();
                    String nama = tp.getNama();
                   LatLng newPos = new LatLng(latitude, longitude);
                    Marker marker1 = mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_small))
                                    .position(newPos)
                                    .title(nama)
                                    .draggable(false)
                    );
                    marker1.setTag(tp.getNama());
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        public void onInfoWindowClick(Marker arg0) {
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng malang = new LatLng(-7.9539075,112.6318367);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(malang,12.0f));
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
