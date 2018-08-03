package com.telkom.lutfi.mytelkomakses;

import android.nfc.Tag;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.telkom.lutfi.mytelkomakses.TeamLeaderTeknisi.ListTeknisiPasangBaru;
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
//                            .snippet("Status : "+getStatus)
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
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LatLng malang = new LatLng(-7.9784695,112.5617419);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(malang,12.0f));
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
