package com.telkom.lutfi.mytelkomakses.TeamLeaderGrupTeknisi;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.telkom.lutfi.mytelkomakses.LoginActivity;
import com.telkom.lutfi.mytelkomakses.R;
import com.telkom.lutfi.mytelkomakses.TeamLeaderTeknisi.TeknisiMenuActivity;
import com.telkom.lutfi.mytelkomakses.TeknisiActivity;
import com.telkom.lutfi.mytelkomakses.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListGrupGangguan extends AppCompatActivity {

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
        toolbar.setTitle("List Grup Gangguan");

        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        mRecyclerView = (RecyclerView) findViewById(R.id.karyawan_list);

        Query query = mFireStore.collection("team").whereEqualTo("jenis", "Gangguan");

        FirestoreRecyclerOptions<Team> options = new FirestoreRecyclerOptions.Builder<Team>()
                .setQuery(query, Team.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Team, ListGrupGangguan.TeamViewHolder>(options) {
            @Override
            public void onBindViewHolder(ListGrupGangguan.TeamViewHolder holder, int position, Team model) {
                String nama_grup = model.getNama_grup();
                String emailTeknisi1 = model.getEmailTeknisi1();
                String emailTeknisi2 = model.getEmailTeknisi2();
                final String id = getSnapshots().getSnapshot(position).getId();

                mFireStore.collection("user").document(emailTeknisi1);

                holder.setNama_grup(nama_grup);
                holder.setEmailtek1(emailTeknisi1);
                holder.setEmailtek2(emailTeknisi2);
                holder.deleteUser(nama_grup, id);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ListGrupGangguan.this, ListDetailGrupGangguan.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public ListGrupGangguan.TeamViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.layout_list_grup, group, false);
                return new ListGrupGangguan.TeamViewHolder(view);
            }


        };
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog showAddLeader = new ListGrupGangguan.AddTeknisiDialog(ListGrupGangguan.this);
                showAddLeader.show();
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListGrupGangguan.this));
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

        void deleteUser(final String nama, final String id) {
            ImageView button = (ImageView) itemView.findViewById(R.id.delete_grup);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListGrupGangguan.this);
                    builder.setMessage("Apakah anda yakin ingin menghapus " + nama);
                    builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mFireStore.collection("team").document(id).delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ListGrupGangguan.this, "Berhasil", Toast.LENGTH_LONG).show();
                                                finish();
                                                Intent intent = new Intent(ListGrupGangguan.this, ListGrupGangguan.class);
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

    private class AddTeknisiDialog extends AlertDialog {
        protected AddTeknisiDialog(@NonNull Context context) {
            super(context);
            LayoutInflater inflater = LayoutInflater.from(ListGrupGangguan.this);
            View addFormView = inflater.inflate(R.layout.create_grup, null);

            final Spinner inputTeknisi1 = (Spinner) addFormView.findViewById(R.id.input_teknisi1);
            final Spinner inputTeknisi2 = (Spinner) addFormView.findViewById(R.id.input_teknisi2);
            final EditText inputNamaGrup = (EditText) addFormView.findViewById(R.id.input_nama_grup);
            final Spinner inputTeamId1 = new Spinner(ListGrupGangguan.this);
            final Spinner inputTeamId2 = new Spinner(ListGrupGangguan.this);
            spin = (Spinner) addFormView.findViewById(R.id.inputjenispekerjaan);

            final List<String> teamList = new ArrayList<>();
            final List<String> teamIdList = new ArrayList<>();

            adaptr = ArrayAdapter.createFromResource(context, R.array.Pekerjaan, android.R.layout.simple_spinner_item);
            adaptr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adaptr);
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + "selected", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            FirebaseFirestore.getInstance().collection("user").whereEqualTo("jenis", "teknisi_gangguan").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    teamList.add(documentSnapshot.getString("email"));
                                    teamIdList.add(documentSnapshot.getId());

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListGrupGangguan.this, android.R.layout.simple_spinner_item, teamList);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    inputTeknisi1.setAdapter(adapter);

                                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ListGrupGangguan.this, android.R.layout.simple_spinner_item, teamList);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    inputTeknisi2.setAdapter(adapter1);

                                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ListGrupGangguan.this, android.R.layout.simple_spinner_item, teamIdList);
                                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    inputTeamId1.setAdapter(adapter2);

                                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(ListGrupGangguan.this, android.R.layout.simple_spinner_item, teamIdList);
                                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    inputTeamId2.setAdapter(adapter3);
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
                    int posisi_E1 = inputTeknisi1.getSelectedItemPosition();
                    int posisi_E2 = inputTeknisi2.getSelectedItemPosition();
                    final String idTeknisi1 = inputTeamId1.getItemAtPosition(posisi_E1).toString();
                    final String idTeknisi2 = inputTeamId2.getItemAtPosition(posisi_E2).toString();
                    final String namaEmail1 = inputTeknisi1.getItemAtPosition(posisi_E1).toString();
                    final String namaEmail2 = inputTeknisi2.getItemAtPosition(posisi_E2).toString();
                    final String namagrup = inputNamaGrup.getText().toString();
                    final String gruporder = spin.getItemAtPosition(post).toString();

                    if (!TextUtils.isEmpty(namaEmail1) && !TextUtils.isEmpty(namaEmail2) && !TextUtils.isEmpty(namagrup)) {
                        Map<String, String> new_user = new HashMap<>();
                        new_user.put("nama_grup", namagrup);
                        new_user.put("idlTeknisi1", idTeknisi1);
                        new_user.put("idTeknisi2", idTeknisi2);
                        new_user.put("emailTeknisi1", namaEmail1);
                        new_user.put("emailTeknisi2", namaEmail2);
                        new_user.put("jenis", gruporder);


                        Map<String, String> grup = new HashMap<>();
                        grup.put("nama_grup", namagrup);
                        mFireStore.collection("user").document(idTeknisi1).update("nama_grup", namagrup)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                        mFireStore.collection("user").document(idTeknisi2).update("nama_grup", namagrup)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                         // table dot primary dot isi
                        mFireStore.collection("team").document(namagrup).set(new_user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ListGrupGangguan.this, "Berhasil menambahkan", Toast.LENGTH_LONG).show();
                                            dismiss();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(ListGrupGangguan.this, "Tolong lenkgapi form", Toast.LENGTH_LONG).show();
                    }
                }
            });
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputNamaGrup.getText().clear();
                }
            });
        }

    }
}
