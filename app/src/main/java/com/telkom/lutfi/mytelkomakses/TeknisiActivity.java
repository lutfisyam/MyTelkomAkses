package com.telkom.lutfi.mytelkomakses;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.telkom.lutfi.mytelkomakses.customs.CustomDialog;
import com.telkom.lutfi.mytelkomakses.model.Order;


import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

public class TeknisiActivity extends AppCompatActivity implements LocationListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;
    private LocationManager locationManager;
    private String provider;
    private String Id_us;
    private String Id_grup;
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layout_teknisi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        dialog = new CustomDialog(TeknisiActivity.this);

        Id_us = intent.getStringExtra("montu");
        Id_grup = intent.getStringExtra("nama_grup");

        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.karyawan_list);

        Query query = mFireStore.collection("order").whereEqualTo("team", Id_grup);


        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<Order, TeknisiActivity.TeamViewHolder>(options) {
            @Override
            public void onBindViewHolder(TeknisiActivity.TeamViewHolder holder, int position, final Order model) {


                final String nama_grup = model.getNama();
                final String almt = model.getAlamat();
                final String kontak = model.getKontak();
                final String jenis = model.getJenis();
                final String id = getSnapshots().getSnapshot(position).getId();

                holder.setNama_grup(nama_grup);
                holder.setEmailtek1(almt);
                holder.setEmailtek2(kontak);

                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (jenis.equals("Pasang Baru")) {
                            Intent intent = new Intent(getApplicationContext(), DetailGrupOrderActivity.class);
                            intent.putExtra("jenis", jenis);
                            intent.putExtra("sc", model.getSc());
                            intent.putExtra("nama", model.getNama());
                            intent.putExtra("alamat", model.getAlamat());
                            intent.putExtra("kontak", model.getKontak());
                            intent.putExtra("ncli", model.getNcli());
                            intent.putExtra("ndem", model.getNdem());
                            intent.putExtra("alproname", model.getAlproname());
                            intent.putExtra("status", model.getStatus());
                            intent.putExtra("no_tiket", model.getNo_tiket());
                            intent.putExtra("no_internet", model.getNo_internet());
                            intent.putExtra("jenis_gangguan", model.getJenis_gangguan());
                            intent.putExtra("bukti", model.getBukti());
                            startActivity(intent);
                        } else if (jenis.equals("Gangguan")) {
                            Intent intent = new Intent(getApplicationContext(), DetailGrupOrderActivity.class);
                            intent.putExtra("jenis", jenis);
                            intent.putExtra("sc", model.getSc());
                            intent.putExtra("nama", model.getNama());
                            intent.putExtra("alamat", model.getAlamat());
                            intent.putExtra("kontak", model.getKontak());
                            intent.putExtra("ncli", model.getNcli());
                            intent.putExtra("ndem", model.getNdem());
                            intent.putExtra("alproname", model.getAlproname());
                            intent.putExtra("status", model.getStatus());
                            intent.putExtra("no_tiket", model.getNo_tiket());
                            intent.putExtra("no_internet", model.getNo_internet());
                            intent.putExtra("jenis_gangguan", model.getJenis_gangguan());
                            intent.putExtra("bukti", model.getBukti());
                            startActivity(intent);
                        }

                    }
                });
            }

            @Override
            public TeknisiActivity.TeamViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.layout_list_teknisi, group, false);

                return new TeknisiActivity.TeamViewHolder(view);
            }
        };

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(TeknisiActivity.this));

        //MAPAS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            Toast.makeText(TeknisiActivity.this, "Lokasi Tidak Aktif", Toast.LENGTH_LONG).show();
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_teknis, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.gantipassword:
                final ProgressDialog progressDialog = new ProgressDialog(TeknisiActivity.this);
                progressDialog.setMessage("Loading");
                progressDialog.show();
                FirebaseFirestore.getInstance().collection("user").document(Id_us)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            DocumentSnapshot documentSnapshot = task.getResult();
                            dialog.showEditTeknisi(Id_us, documentSnapshot.getString("nip"), documentSnapshot.getString("nama"),
                                    documentSnapshot.getString("email"), documentSnapshot.getString("pass"));
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(TeknisiActivity.this, "Gagal", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 1000, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);

   }

    @Override
    public void onLocationChanged(Location location) {
        double lat = (double) (location.getLatitude());
        double lng = (double) (location.getLongitude());


        mFireStore.collection("user").document(Id_us).update("latitude", lat,
                "longlitude", lng)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    private class TeamViewHolder extends RecyclerView.ViewHolder {
        public TeamViewHolder(View itemView) {
            super(itemView);

        }

        void setNama_grup(String nama) {
            TextView textView = (TextView) itemView.findViewById(R.id.namagrup);
            textView.setText(nama);
        }

        void setEmailtek1(String email1) {
            TextView textView = (TextView) itemView.findViewById(R.id.emailteknisi1);
            textView.setText(email1);
        }

        void setEmailtek2(String email2) {
            TextView textView = (TextView) itemView.findViewById(R.id.emailteknisi2);
            textView.setText(email2);
        }



    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }

    }


}
