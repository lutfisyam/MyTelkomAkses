package com.telkom.lutfi.mytelkomakses.TeamLeaderGrupTeknisi;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.telkom.lutfi.mytelkomakses.DetailGrupOrderActivity;
import com.telkom.lutfi.mytelkomakses.R;
import com.telkom.lutfi.mytelkomakses.model.Order;

import java.util.ArrayList;

public class ListDetailGrupPasangBaru extends AppCompatActivity {

    Spinner spin;
    ArrayAdapter<CharSequence> adaptr;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layout);
        Toolbar toolbar =(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        String Id_grup = intent.getStringExtra("montu");

        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.karyawan_list);

        Query query = mFireStore.collection("order").whereEqualTo("team", Id_grup);

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<Order, ListDetailGrupPasangBaru.TeamViewHolder>(options) {
            @Override
            public void onBindViewHolder(ListDetailGrupPasangBaru.TeamViewHolder holder, int position, final Order model) {


                final String nama_grup = model.getNama();
                final String almt = model.getAlamat();
                final String kontak = model.getKontak();
                final String id = getSnapshots().getSnapshot(position).getId();

                holder.setNama_grup(nama_grup);
                holder.setEmailtek1(almt);
                holder.setEmailtek2(kontak);
                holder.deleteUser(nama_grup, id);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getApplicationContext(),DetailGrupOrderActivity.class);
                        intent.putExtra("id",nama_grup);
                        intent.putExtra("sc", model.getSc());
                        intent.putExtra("nama", model.getNama());
                        intent.putExtra("alamat", model.getAlamat());
                        intent.putExtra("kontak", model.getKontak());
                        intent.putExtra("ncli", model.getNcli());
                        intent.putExtra("ndem", model.getNdem());
                        intent.putExtra("alproname", model.getAlproname());
                        intent.putExtra("status", model.getStatus());
                        startActivity(intent);
//                        Toast.makeText(ListDetailGrupPasangBaru.this, id, Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public ListDetailGrupPasangBaru.TeamViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.layout_list_grup, group, false);

                return new ListDetailGrupPasangBaru.TeamViewHolder(view);
            }
        };

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListDetailGrupPasangBaru.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        switch (id){
            case R.id.grupTeknisipb:
                Intent i = new Intent (getApplicationContext(),ListGrupPasang.class);
                startActivity(i);
                super.onBackPressed();
                break;
            case R.id.grupTeknisigangguan:
                Intent I = new Intent (getApplicationContext(),ListGrupGangguan.class);
                startActivity(I);
                super.onBackPressed();
                break;
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


    private void ambilData(DocumentSnapshot documentSnapshot, ArrayList<String> team) {

        {
            team.clear();
            String nama = documentSnapshot.getString("nama_grup");
            team.add(nama);
        }

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

        void deleteUser( final String nama, final String id) {
            ImageView button = (ImageView) itemView.findViewById(R.id.delete_grup);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListDetailGrupPasangBaru.this);
                    builder.setMessage("Apakah anda yakin ingin menghapus " + nama);
                    builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mFireStore.collection("team").document(id).delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ListDetailGrupPasangBaru.this, "Berhasil", Toast.LENGTH_LONG).show();
                                                finish();
                                                Intent intent = new Intent(ListDetailGrupPasangBaru.this, ListGrupGangguan.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });


                        }
                    });

                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                }
            });
        }
    }
}
