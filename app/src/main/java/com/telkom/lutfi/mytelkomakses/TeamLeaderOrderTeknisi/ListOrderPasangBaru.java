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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOrderPasangBaru extends AppCompatActivity {
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.karyawan_list);

        Query query = mFireStore.collection("order").whereEqualTo("jenis", "Pasang Baru");

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Order, ListOrderPasangBaru.UserViewHolder>(options) {
            @Override
            public void onBindViewHolder(ListOrderPasangBaru.UserViewHolder holder, int position, Order model) {
                String alamat = model.getAlamat();
                String kontak = model.getKontak();
                String nama = model.getNama();
                String status =model.getStatus();
                final String id_order = getSnapshots().getSnapshot(position).getId();

                holder.setNama(nama);
                holder.setAlamat(alamat);
                holder.setKontak(kontak);
                holder.setStatus(status);
                holder.deleteUser(nama, id_order);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ListOrderPasangBaru.this, id_order, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public ListOrderPasangBaru.UserViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.layout_order_indicator, group, false);

                return new ListOrderPasangBaru.UserViewHolder(view);
            }


        };


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog showAddLeader = new ListOrderPasangBaru.AddOrderDialog(ListOrderPasangBaru.this);
                showAddLeader.show();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListOrderPasangBaru.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
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

    private class UserViewHolder extends RecyclerView.ViewHolder {
        public UserViewHolder(View itemView) {
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

        void setAlamat(String email1) {
            TextView textView = (TextView) itemView.findViewById(R.id.emailteknisi1);
            textView.setText(email1);
        }

        void setKontak(String email2) {
            TextView textView = (TextView) itemView.findViewById(R.id.emailteknisi2);
            textView.setText(email2);
        }

        void deleteUser( final String nama, final String id) {
            ImageView button = (ImageView) itemView.findViewById(R.id.delete_grup);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListOrderPasangBaru.this);
                    builder.setMessage("Apakah anda yakin ingin menghapus order" + nama);
                    builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mFireStore.collection("order").document(id).delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ListOrderPasangBaru.this, "Berhasil", Toast.LENGTH_LONG).show();
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

            LayoutInflater inflater = LayoutInflater.from(ListOrderPasangBaru.this);
            View addFormView = inflater.inflate(R.layout.create_order, null);

            final EditText inputSc = (EditText) addFormView.findViewById(R.id.enter_sc);
            final EditText inputNama = (EditText) addFormView.findViewById(R.id.enter_namapel);
            final EditText inputAlamat = (EditText) addFormView.findViewById(R.id.enter_alamat);
            final EditText inputKontak = (EditText) addFormView.findViewById(R.id.enter_kontak);
            final EditText inputNcli = (EditText) addFormView.findViewById(R.id.enter_ncli);
            final EditText inputNdem = (EditText) addFormView.findViewById(R.id.enter_ndem);
            final EditText inputAlproname = (EditText) addFormView.findViewById(R.id.enter_alproname);
            final Spinner inputTeamName = (Spinner) addFormView.findViewById(R.id.enter_grupteamteknisi);
            spin = (Spinner) addFormView.findViewById(R.id.enter_gruporder);

            final List<String> teamList = new ArrayList<>();

            adaptr = ArrayAdapter.createFromResource(context, R.array.Pekerjaan, android.R.layout.simple_spinner_item);
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

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListOrderPasangBaru.this, android.R.layout.simple_spinner_item, teamList);
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
                    final String sc = inputSc.getText().toString();
                    final String nama = inputNama.getText().toString();
                    final String alamat = inputAlamat.getText().toString();
                    final String Kontak = inputKontak.getText().toString();
                    final String ncli = inputNcli.getText().toString();
                    final String ndem = inputNdem.getText().toString();
                    final String alproname = inputAlproname.getText().toString();
                    final String namaTeam = inputTeamName.getItemAtPosition(posisi_E1).toString();
                    final String gruporder = spin.getItemAtPosition(post).toString();

                    if (!TextUtils.isEmpty(sc)
                            && !TextUtils.isEmpty(nama)
                            && !TextUtils.isEmpty(alamat)
                            && !TextUtils.isEmpty(ncli)
                            && !TextUtils.isEmpty(ndem)
                            && !TextUtils.isEmpty(alproname)
                            ) {

                        Map<String, String> new_user = new HashMap<>();
                        new_user.put("sc", sc);
                        new_user.put("nama", nama);
                        new_user.put("alamat", alamat);
                        new_user.put("kontak", Kontak);
                        new_user.put("ncli", ncli);
                        new_user.put("ndem", ndem);
                        new_user.put("alproname", alproname);
                        new_user.put("jenis", gruporder);
                        new_user.put("team", namaTeam);
                        new_user.put("status", "belum");
                        // table dot primary dot isi
                        mFireStore.collection("order").document(sc).set(new_user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ListOrderPasangBaru.this, "Berhasil menambahkan", Toast.LENGTH_LONG).show();
                                            dismiss();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(ListOrderPasangBaru.this, "Tolong lenkgapi form", Toast.LENGTH_LONG).show();
                    }
                }
            });

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputSc.getText().clear();
                    inputNama.getText().clear();
                    inputAlamat.getText().clear();
                    inputKontak.getText().clear();
                    inputNcli.getText().clear();
                    inputNdem.getText().clear();
                    inputAlproname.getText().clear();
                }
            });
        }

    }
}
