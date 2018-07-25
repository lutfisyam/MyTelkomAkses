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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.telkom.lutfi.mytelkomakses.entitas.User;

import java.util.HashMap;
import java.util.Map;

public class ListTeamLeader extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layout);
        Toolbar toolbar =(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);



        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.karyawan_list);

        Query query = mFireStore.collection("user").whereEqualTo("jenis","leader");

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

            adapter = new FirestoreRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            public void onBindViewHolder(UserViewHolder holder, int position, User model) {
                String email_teknisi = model.getEmail();
                String pass_teknisi = model.getPass();
                String nama_teknisi = model.getNama();
                final String id_teknisi = getSnapshots().getSnapshot(position).getId();

                holder.setNama(nama_teknisi);
                holder.setEmail(email_teknisi);
                holder.deleteUser(email_teknisi, pass_teknisi, nama_teknisi, id_teknisi);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ListTeamLeader.this, id_teknisi, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.layout_list_name, group, false);

                return new UserViewHolder(view);
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog showAddLeader = new AddLeaderDialog(ListTeamLeader.this);
                showAddLeader.show();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListTeamLeader.this));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        switch (id){
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

        void deleteUser(final String email, final String pass, final String nama, final String id) {
            ImageView button = (ImageView) itemView.findViewById(R.id.delete_karyawan);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListTeamLeader.this);
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
                                                                    Toast.makeText(ListTeamLeader.this, "Berhasil", Toast.LENGTH_LONG).show();
                                                                    finish();
                                                                    Intent intent = new Intent(ListTeamLeader.this, LoginActivity.class);
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

    private class AddLeaderDialog extends AlertDialog {

        protected AddLeaderDialog(@NonNull Context context) {
            super(context);

            LayoutInflater inflater = LayoutInflater.from(ListTeamLeader.this);
            View addFormView = inflater.inflate(R.layout.create_team_leader, null);

            final EditText inputId = (EditText) addFormView.findViewById(R.id.enter_id);
            final EditText inputNama = (EditText) addFormView.findViewById(R.id.enter_nama);
            final EditText inputEmail = (EditText) addFormView.findViewById(R.id.enter_Email);
            final EditText inputPassword = (EditText) addFormView.findViewById(R.id.enter_Password);

            Button simpan = (Button) addFormView.findViewById(R.id.btn_simpan_leader);
            Button reset = (Button) addFormView.findViewById(R.id.btn_reset_leader);

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
                                            Map<String, String> new_user = new HashMap<>();
                                            new_user.put("nip", id);
                                            new_user.put("nama", nama);
                                            new_user.put("email", email);
                                            new_user.put("pass", password);
                                            new_user.put("jenis", "leader");

                                            // table dot primary dot isi
                                            mFireStore.collection("user").document(newUid).set(new_user)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ListTeamLeader.this, "Berhasil menambahkan", Toast.LENGTH_LONG).show();
                                                                dismiss();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(ListTeamLeader.this, "Gagal menambahkan", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(ListTeamLeader.this, "Tolong lenkgapi form", Toast.LENGTH_LONG).show();
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

