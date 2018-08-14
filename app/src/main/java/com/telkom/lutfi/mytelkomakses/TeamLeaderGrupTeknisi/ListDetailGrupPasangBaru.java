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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.telkom.lutfi.mytelkomakses.DetailOrderLayoutTL;
import com.telkom.lutfi.mytelkomakses.R;
import com.telkom.lutfi.mytelkomakses.model.Order;


public class ListDetailGrupPasangBaru extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        String Id_grup = intent.getStringExtra("id");

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
                final String status = model.getStatus();
                final String id = getSnapshots().getSnapshot(position).getId();

                holder.setNama_grup(nama_grup);
                holder.setEmailtek1(almt);
                holder.setEmailtek2(kontak);
                holder.setStatus(status);
                holder.deleteUser(nama_grup, id);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), DetailOrderLayoutTL.class);
                        intent.putExtra("id", nama_grup);
                        intent.putExtra("sc", model.getSc());
                        intent.putExtra("nama", model.getNama());
                        intent.putExtra("alamat", model.getAlamat());
                        intent.putExtra("kontak", model.getKontak());
                        intent.putExtra("ncli", model.getNcli());
                        intent.putExtra("ndem", model.getNdem());
                        intent.putExtra("alproname", model.getAlproname());
                        intent.putExtra("status", model.getStatus());
                        intent.putExtra("bukti", model.getBukti());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public ListDetailGrupPasangBaru.TeamViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.layout_order_indicator, group, false);
                return new ListDetailGrupPasangBaru.TeamViewHolder(view);
            }
        };

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListDetailGrupPasangBaru.this));
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

    private class TeamViewHolder extends RecyclerView.ViewHolder {
        public TeamViewHolder(View itemView) {
            super(itemView);
        }

        void setStatus(String status) {
            ImageView imageView1 = (ImageView) itemView.findViewById(R.id.in_belum);
            ImageView imageView2 = (ImageView) itemView.findViewById(R.id.in_proses);
            ImageView imageView3 = (ImageView) itemView.findViewById(R.id.in_selesai);
            ImageView imageView4 = (ImageView) itemView.findViewById(R.id.in_ayahab);
            if (status.equals("belum")) {
                imageView1.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.GONE);
                imageView3.setVisibility(View.GONE);
                imageView4.setVisibility(View.GONE);
            } else if (status.equals("proses")) {
                imageView1.setVisibility(View.GONE);
                imageView2.setVisibility(View.VISIBLE);
                imageView3.setVisibility(View.GONE);
                imageView4.setVisibility(View.GONE);
            } else if (status.equals("selesai")) {
                imageView1.setVisibility(View.GONE);
                imageView2.setVisibility(View.GONE);
                imageView3.setVisibility(View.VISIBLE);
                imageView4.setVisibility(View.GONE);
            } else if (status.equals("gagal")){
                imageView1.setVisibility(View.GONE);
                imageView2.setVisibility(View.GONE);
                imageView3.setVisibility(View.GONE);
                imageView4.setVisibility(View.VISIBLE);
            }
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

        void deleteUser(final String nama, final String id) {
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
