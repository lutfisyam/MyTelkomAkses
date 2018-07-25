package com.telkom.lutfi.mytelkomakses;

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
import android.widget.Adapter;
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
import com.telkom.lutfi.mytelkomakses.entitas.User;

import java.util.ArrayList;
import java.util.HashMap;
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

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<User, ListOrderGangguan.UserViewHolder>(options) {
            @Override
            public void onBindViewHolder(ListOrderGangguan.UserViewHolder holder, int position, User model) {
                String email_karyawan = model.getEmail();
                String pass_karyawan = model.getPass();
                String nama_karyawan = model.getNama();
                final String id_karyawan = getSnapshots().getSnapshot(position).getId();

                holder.setNama(nama_karyawan);
                holder.setEmail(email_karyawan);
                holder.deleteUser(email_karyawan, pass_karyawan, nama_karyawan, id_karyawan);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ListOrderGangguan.this, id_karyawan, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public ListOrderGangguan.UserViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.layout_list_name, group, false);

                return new ListOrderGangguan.UserViewHolder(view);
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
            case R.id.OrderGangguan:
                Intent i = new Intent (getApplicationContext(),ListOrderGangguan.class);
                startActivity(i);
                super.onBackPressed();
                break;
            case R.id.OrderPasang:
                Intent I = new Intent (getApplicationContext(),ListOrderPasangBaru.class);
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
            String nama = documentSnapshot.getString("nama");
            team.add(nama);
        }

    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        public UserViewHolder(View itemView) {
            super(itemView);

        }

        void setNama(String nama) {
            TextView textView = (TextView) itemView.findViewById(R.id.karyawan_name);
            textView.setText(nama);
        }

        void setEmail(String email) {
            TextView textView = (TextView) itemView.findViewById(R.id.karyawan_email);
            textView.setText(email);
        }

        void deleteUser(final String email, final String pass, final String nama, final String id) {
            ImageView button = (ImageView) itemView.findViewById(R.id.delete_karyawan);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListOrderGangguan.this);
                    builder.setMessage("Apakah anda yakin ingin menghapus user" + nama);
                    builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signInWithEmailAndPassword(email, pass)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                mFireStore.collection("user").document(id).delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(ListOrderGangguan.this, "Berhasil", Toast.LENGTH_LONG).show();
                                                                    finish();
                                                                    Intent intent = new Intent(ListOrderGangguan.this, LoginActivity.class);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        });
                                                mAuth.getCurrentUser().delete();
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
            View addFormView = inflater.inflate(R.layout.create_order, null);

            final EditText inputSc = (EditText) addFormView.findViewById(R.id.enter_sc);
            final EditText inputNama = (EditText) addFormView.findViewById(R.id.enter_namapel);
            final EditText inputAlamat = (EditText) addFormView.findViewById(R.id.enter_alamat);
            final EditText inputKontak = (EditText) addFormView.findViewById(R.id.enter_kontak);
            final EditText inputNcli = (EditText) addFormView.findViewById(R.id.enter_ncli);
            final EditText inputNdem = (EditText) addFormView.findViewById(R.id.enter_ndem);
            final EditText inputAlproname = (EditText) addFormView.findViewById(R.id.enter_alproname);
            spin = (Spinner) addFormView.findViewById(R.id.enter_gruporder);

//            adaptr=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,R.array.Pekerjaan);
            adaptr = ArrayAdapter.createFromResource(context,R.array.Pekerjaan,android.R.layout.simple_spinner_item);
            adaptr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adaptr);
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+ "selected",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Button simpan = (Button) addFormView.findViewById(R.id.btn_simpan_order);
            Button reset = (Button) addFormView.findViewById(R.id.btn_reset_order);

            setView(addFormView);

            simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int post = spin.getSelectedItemPosition();
                    final String sc = inputSc.getText().toString();
                    final String nama = inputNama.getText().toString();
                    final String alamat = inputAlamat.getText().toString();
                    final String Kontak = inputKontak.getText().toString();
                    final String ncli = inputNcli.getText().toString();
                    final String ndem = inputNdem.getText().toString();
                    final String alproname = inputAlproname.getText().toString();
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
                        new_user.put("Alproname", alproname);
                        new_user.put("team", gruporder);
                        // table dot primary dot isi
                        mFireStore.collection("order").document(sc).set(new_user)
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
