package com.telkom.lutfi.mytelkomakses.TeamLeaderOrderTeknisi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.telkom.lutfi.mytelkomakses.LoginActivity;
import com.telkom.lutfi.mytelkomakses.R;
import com.telkom.lutfi.mytelkomakses.model.Order;
import com.telkom.lutfi.mytelkomakses.model.User;
import com.telkom.lutfi.mytelkomakses.post.PostData;
import com.telkom.lutfi.mytelkomakses.post.PostGangguan;
//import com.telkom.lutfi.mytelkomakses.post.PostGangguan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOrderGangguan extends AppCompatActivity {
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


        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.karyawan_list);

        Query query = mFireStore.collection("order").whereEqualTo("jenis", "Gangguan");

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Order, ListOrderGangguan.OrderViewHolder>(options) {
            @Override
            public void onBindViewHolder(ListOrderGangguan.OrderViewHolder holder, int position, Order model) {
                String alamat = model.getAlamat();
                String kontak = model.getKontak();
                String nama = model.getNama();
                String status = model.getStatus();
                final String id_order = getSnapshots().getSnapshot(position).getId();

                holder.setNama(nama);
                holder.setAlamat(alamat);
                holder.setKontak(kontak);
                holder.setStatus(status);
                holder.deleteUser(nama, id_order);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ListOrderGangguan.this, id_order, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public ListOrderGangguan.OrderViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.layout_order_indicator, group, false);

                return new ListOrderGangguan.OrderViewHolder(view);
            }


        };


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog showAddLeader = new ListOrderGangguan.AddOrderDialog(ListOrderGangguan.this);
                showAddLeader.show();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListOrderGangguan.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu_order, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        switch (id){
            case R.id.PrintGangguan:
                Intent i = new Intent(getApplicationContext(), PostGangguan.class);
                startActivity(i);
                super.onBackPressed();
                break;
            case R.id.PrintPasang:
                Intent I = new Intent(getApplicationContext(), PostData.class);
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


    private class OrderViewHolder extends RecyclerView.ViewHolder {
        public OrderViewHolder(View itemView) {
            super(itemView);
        }

        void setStatus(String status) {
            ImageView imageView1 = (ImageView) itemView.findViewById(R.id.in_belum);
            ImageView imageView2 = (ImageView) itemView.findViewById(R.id.in_proses);
            ImageView imageView3 = (ImageView) itemView.findViewById(R.id.in_selesai);
            if(status.equals("belum")) {
                imageView1.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.GONE);
                imageView3.setVisibility(View.GONE);
            } else if (status.equals("proses")) {

                imageView1.setVisibility(View.GONE);
                imageView2.setVisibility(View.VISIBLE);
                imageView3.setVisibility(View.GONE);
            } else {

                imageView1.setVisibility(View.GONE);
                imageView2.setVisibility(View.GONE);
                imageView3.setVisibility(View.VISIBLE);
            }
        }

        void setNama(String nama) {
            TextView textView = (TextView) itemView.findViewById(R.id.namagrup);
            textView.setText(nama);
        }

        void setAlamat(String email) {
            TextView textView = (TextView) itemView.findViewById(R.id.emailteknisi1);
            textView.setText(email);
        }

        void setKontak(String email) {
            TextView textView = (TextView) itemView.findViewById(R.id.emailteknisi2);
            textView.setText(email);
        }


        void deleteUser( final String nama, final String id) {
            ImageView button = (ImageView) itemView.findViewById(R.id.delete_grup);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListOrderGangguan.this);
                    builder.setMessage("Apakah anda yakin ingin menghapus order" + nama);
                    builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mFireStore.collection("order").document(id).delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ListOrderGangguan.this, "Berhasil", Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(getIntent());
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


    private class AddOrderDialog extends AlertDialog {

        protected AddOrderDialog(@NonNull Context context) {
            super(context);

            LayoutInflater inflater = LayoutInflater.from(ListOrderGangguan.this);
            View addFormView = inflater.inflate(R.layout.create_order_gangguan, null);

            final EditText inputNotic = (EditText) addFormView.findViewById(R.id.enter_tiket);
            final EditText inputNapel = (EditText) addFormView.findViewById(R.id.enter_namapel);
            final EditText inputLokasi = (EditText) addFormView.findViewById(R.id.enter_alamat);
            final EditText inputTlpn = (EditText) addFormView.findViewById(R.id.enter_kontak);
            final EditText inputNoin = (EditText) addFormView.findViewById(R.id.enter_internet);
            final EditText inputjenisGG = (EditText) addFormView.findViewById(R.id.enter_jenisgg);
            final Spinner inputTeamName = (Spinner) addFormView.findViewById(R.id.enter_grupteamteknisi);
            spin = (Spinner) addFormView.findViewById(R.id.enter_gruporder);

            final List<String> teamList = new ArrayList<>();

            adaptr = ArrayAdapter.createFromResource(context,R.array.Pekerjaan,android.R.layout.simple_spinner_item);
            adaptr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adaptr);

            FirebaseFirestore.getInstance().collection("team").orderBy("jenis").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    teamList.add(documentSnapshot.getString("nama_grup"));
//                                    teamIdList.add(documentSnapshot.getId());

//                                    Toast.makeText(ListTeknisi.this, documentSnapshot.getString("nama"), Toast.LENGTH_LONG).show();

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListOrderGangguan.this, android.R.layout.simple_spinner_item, teamList);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    inputTeamName.setAdapter(adapter);


                                }
                            }
                        }
                    });

            Button simpan = (Button) addFormView.findViewById(R.id.btn_simpan_order);
            Button reset = (Button) addFormView.findViewById(R.id.btn_reset_order);

            setView(addFormView);

            simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int post = spin.getSelectedItemPosition();
                    int posisi_E1 = inputTeamName.getSelectedItemPosition();
                    final String notiket = inputNotic.getText().toString();
                    final String nama = inputNapel.getText().toString();
                    final String lokasi = inputLokasi.getText().toString();
                    final String tlpn = inputTlpn.getText().toString();
                    final String nointernet = inputNoin.getText().toString();
                    final String jenisgg = inputjenisGG.getText().toString();
                    final String namaTeam = inputTeamName.getItemAtPosition(posisi_E1).toString();
                    final String gruporder = spin.getItemAtPosition(post).toString();

                    if (!TextUtils.isEmpty(notiket)
                            && !TextUtils.isEmpty(nama)
                            && !TextUtils.isEmpty(lokasi)
                            && !TextUtils.isEmpty(tlpn)
                            && !TextUtils.isEmpty(nointernet)
                            && !TextUtils.isEmpty(jenisgg)
                            ) {

                        Map<String, Object> new_user = new HashMap<>();
                        new_user.put("no_tiket", notiket);
                        new_user.put("nama", nama);
                        new_user.put("alamat", lokasi);
                        new_user.put("kontak", tlpn);
                        new_user.put("no_internet", nointernet);
                        new_user.put("tgl", new Date());
                        new_user.put("jenis_gangguan", jenisgg);
                        new_user.put("jenis", gruporder);
                        new_user.put("team", namaTeam);
                        new_user.put("status", "belum");
                        // table dot primary dot isi
                        mFireStore.collection("order").document().set(new_user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ListOrderGangguan.this, "Berhasil menambahkan", Toast.LENGTH_LONG).show();
                                            dismiss();
                                        }
                                    }
                                });
                    }   else {
                        Toast.makeText(ListOrderGangguan.this, "Tolong lenkgapi form", Toast.LENGTH_LONG).show();
                    }
                }
            });

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputNotic.getText().clear();
                    inputNapel.getText().clear();
                    inputLokasi.getText().clear();
                    inputTlpn.getText().clear();
                    inputNoin.getText().clear();
                    inputjenisGG.getText().clear();
                }
            });
        }

    }
}
