package com.telkom.lutfi.mytelkomakses.TeamLeaderTeknisi;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.telkom.lutfi.mytelkomakses.TeamLeaderGrupTeknisi.ListGrupGangguan;
import com.telkom.lutfi.mytelkomakses.TeamLeaderGrupTeknisi.ListGrupPasang;
import com.telkom.lutfi.mytelkomakses.LoginActivity;
import com.telkom.lutfi.mytelkomakses.R;
import com.telkom.lutfi.mytelkomakses.TeamLeaderGrupTeknisi.MenuGrupTeknisi;
import com.telkom.lutfi.mytelkomakses.TeknisiActivity;
import com.telkom.lutfi.mytelkomakses.customs.CustomDialog;
import com.telkom.lutfi.mytelkomakses.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListTeknisiPasangBaru extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;

    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("List Pasang Baru");

        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        dialog = new CustomDialog(ListTeknisiPasangBaru.this);

        mRecyclerView = (RecyclerView) findViewById(R.id.karyawan_list);

        Query query = mFireStore.collection("user").whereEqualTo("jenis", "teknisi_pasang_baru");

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<User, ListTeknisiPasangBaru.UserViewHolder>(options) {
            @Override
            public void onBindViewHolder(ListTeknisiPasangBaru.UserViewHolder holder, int position, User model) {
                final String id_teknisi = getSnapshots().getSnapshot(position).getId();
                final String nip_teknisi = model.getNip();
                final String nama_teknisi = model.getNama();
                final String email_teknisi = model.getEmail();
                final String pass_teknisi = model.getPass();

                holder.setNama(nama_teknisi);
                holder.setEmail(email_teknisi);
                holder.deleteUser(email_teknisi, pass_teknisi, nama_teknisi, id_teknisi);
                holder.updateUser(id_teknisi, nip_teknisi, nama_teknisi, email_teknisi, pass_teknisi);

            }
            @Override
            public ListTeknisiPasangBaru.UserViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.layout_list_name, group, false);
                return new ListTeknisiPasangBaru.UserViewHolder(view);
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog showAddLeader = new ListTeknisiPasangBaru.AddTeknisiDialog(ListTeknisiPasangBaru.this);
                showAddLeader.show();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListTeknisiPasangBaru.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.list_teknisi:
                Intent i = new Intent(getApplicationContext(), TeknisiMenuActivity.class);
                startActivity(i);
                super.onBackPressed();
                break;
            case R.id.list_grup:
                Intent I = new Intent(getApplicationContext(), MenuGrupTeknisi.class);
                startActivity(I);
                super.onBackPressed();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
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

        void setNama(String nama) {
            TextView textView = (TextView) itemView.findViewById(R.id.karyawan_name);
            textView.setText(nama);
        }

        void setEmail(String email) {
            TextView textView = (TextView) itemView.findViewById(R.id.karyawan_email);
            textView.setText(email);
        }

        void updateUser(final String UID, final String nip, final String nama, final String email, final String pass) {
            ImageView update = itemView.findViewById(R.id.edit_user);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.showEditTeknisi(UID, nip, nama, email, pass);
                }
            });
        }

        void deleteUser(final String email, final String pass, final String nama, final String id) {
            ImageView button = (ImageView) itemView.findViewById(R.id.delete_karyawan);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListTeknisiPasangBaru.this);
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
                                                                    Toast.makeText(ListTeknisiPasangBaru.this, "Berhasil", Toast.LENGTH_LONG).show();
                                                                    finish();
                                                                    Intent intent = new Intent(ListTeknisiPasangBaru.this, TeknisiMenuActivity.class);
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

    private class AddTeknisiDialog extends AlertDialog {
        protected AddTeknisiDialog(@NonNull Context context) {
            super(context);
            LayoutInflater inflater = LayoutInflater.from(ListTeknisiPasangBaru.this);
            View addFormView = inflater.inflate(R.layout.create_teknisi, null);

            final EditText inputId = (EditText) addFormView.findViewById(R.id.enter_id);
            final EditText inputNama = (EditText) addFormView.findViewById(R.id.enter_nama);
            final EditText inputEmail = (EditText) addFormView.findViewById(R.id.enter_Email);
            final EditText inputPassword = (EditText) addFormView.findViewById(R.id.enter_Password);

            Button simpan = (Button) addFormView.findViewById(R.id.btn_simpan_teknisi);
            Button reset = (Button) addFormView.findViewById(R.id.btn_reset_teknisi);

            setView(addFormView);

            simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String id = inputId.getText().toString();
                    final String nama = inputNama.getText().toString();
                    final String email = inputEmail.getText().toString();
                    final String password = inputPassword.getText().toString();
                    if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(nama) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String newUid = mAuth.getCurrentUser().getUid();
                                            Map<String, Object> new_user = new HashMap<>();
                                            new_user.put("nip", id);
                                            new_user.put("nama", nama);
                                            new_user.put("email", email);
                                            new_user.put("pass", password);
                                            new_user.put("jenis", "teknisi_pasang_baru");
                                            new_user.put("job", "belum");
                                            new_user.put("longlitude", new Double(152.7130015));
                                            new_user.put("latitude", new Double (-27.3818631));
                                            // table dot primary dot isi
                                            mFireStore.collection("user").document(newUid).set(new_user)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ListTeknisiPasangBaru.this, "Berhasil menambahkan", Toast.LENGTH_LONG).show();
                                                                dismiss();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(ListTeknisiPasangBaru.this, "Gagal menambahkan", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(ListTeknisiPasangBaru.this, "Tolong lenkgapi form", Toast.LENGTH_LONG).show();
                    }
                }
            });

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputId.getText().clear();
                    inputNama.getText().clear();
                    inputEmail.getText().clear();
                    inputPassword.getText().clear();
                }
            });
        }

    }
}
