package com.telkom.lutfi.mytelkomakses;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class DetailGrupOrderActivity extends AppCompatActivity {

    private TextView sc, nama, alamat, kontak, ncli, ndem, alpro, status, nonin, notic, jenisgg;
    String stus;
    FirebaseAuth mAuth;
    FirebaseFirestore mCurrentUserRef;
    private ImageView fotoBuktiImage;

    private String SCid;
    private String jenis;
    private String noTic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        SCid = getIntent().getStringExtra("sc");
        noTic = getIntent().getStringExtra("no_tiket");
        jenis = getIntent().getStringExtra("jenis");
        stus = getIntent().getStringExtra("status");

        if (jenis.equals("Pasang Baru")) {
            setContentView(R.layout.activity_detail_grup_order);
            final Intent intent = getIntent();
            final String nama_grup = intent.getStringExtra("sc");

            sc = (TextView) findViewById(R.id.showsc);
            nama = (TextView) findViewById(R.id.shownama);
            alamat = (TextView) findViewById(R.id.showalamat);
            kontak = (TextView) findViewById(R.id.showkontak);
            ncli = (TextView) findViewById(R.id.showncli);
            ndem = (TextView) findViewById(R.id.showndem);
            alpro = (TextView) findViewById(R.id.showalpro);
            status = (TextView) findViewById(R.id.showstatus);
            fotoBuktiImage = (ImageView) findViewById(R.id.detail_foto_bukti);

            // Pasang foto bukti
            Picasso.get().load(getIntent().getStringExtra("bukti")).into(fotoBuktiImage);

            sc.setText(SCid);
            nama.setText(getIntent().getStringExtra("nama"));
            alamat.setText(getIntent().getStringExtra("alamat"));
            kontak.setText(getIntent().getStringExtra("kontak"));
            ncli.setText(getIntent().getStringExtra("ncli"));
            ndem.setText(getIntent().getStringExtra("ndem"));
            alpro.setText(getIntent().getStringExtra("alproname"));
            status.setText(getIntent().getStringExtra("status"));
            stus = status.getText().toString();
            mAuth = FirebaseAuth.getInstance();
            final String currentUID = mAuth.getCurrentUser().getUid(); // Mengambil id telogin
            final String[] namagrup = new String[1];
            mCurrentUserRef = FirebaseFirestore.getInstance();
            mCurrentUserRef.collection("user").document(currentUID)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.w("Listen failed.", "Listen failed.", e);
                                return;
                            }

                            if (snapshot != null && snapshot.exists()) {
                                String jenis = snapshot.getString("jenis");

                                if (jenis != null && (jenis.equals("teknisi_pasang_baru") || jenis.equals("teknisi_gangguan"))) {

                                    if (stus.equals("belum")) {
                                        Button but = (Button) findViewById(R.id.kerjakan);
                                        but.setVisibility(View.VISIBLE);
                                        Button but2 = (Button) findViewById(R.id.selesai);
                                        but2.setVisibility(View.GONE);

                                    } else if (stus.equals("proses")) {
                                        Button but = (Button) findViewById(R.id.selesai);
                                        but.setVisibility(View.VISIBLE);
                                        Button but2 = (Button) findViewById(R.id.kerjakan);
                                        but2.setVisibility(View.GONE);

                                    } else if (stus.equals("selesai")) {
                                        Button but = (Button) findViewById(R.id.selesai);
                                        but.setVisibility(View.GONE);
                                        Button but2 = (Button) findViewById(R.id.kerjakan);
                                        but2.setVisibility(View.GONE);

                                    } else {
                                        Button but = (Button) findViewById(R.id.kerjakan);
                                        but.setVisibility(View.VISIBLE);
                                        Button but2 = (Button) findViewById(R.id.selesai);
                                        but2.setVisibility(View.GONE);
                                    }


                                } else {
                                    Button but = (Button) findViewById(R.id.kerjakan);
                                    but.setVisibility(View.GONE);
                                    Button but2 = (Button) findViewById(R.id.selesai);
                                    but2.setVisibility(View.GONE);


                                }

                            } else {
                                Log.d("Current data: null", "Current data: null");

                            }
                        }
                    });


            Button i = (Button) findViewById(R.id.kerjakan);
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailGrupOrderActivity.this, InputReportTeknisiActivity.class);
                    intent.putExtra("sc", SCid);
                    intent.putExtra("jenis", jenis);
                    startActivity(intent);
                    finish();
                }
            });

            Button btn = (Button) findViewById(R.id.selesai);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailGrupOrderActivity.this, InputFinishReportActivity.class);
                    intent.putExtra("scid", SCid);
                    intent.putExtra("jenis", jenis);
                    startActivity(intent);
                    finish();
                }
            });
            Button btn_call = (Button) findViewById(R.id.btn_call);
            btn_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialContactPhone(kontak.getText().toString());
                }

                private void dialContactPhone(final String phoneNumber) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", String.valueOf(phoneNumber), null)));
                }
            });
            Button btn_wa = (Button) findViewById(R.id.btn_wa);
            btn_wa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String text = "";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + kontak.getText().toString() + "&text=" + text));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {


            setContentView(R.layout.activity_detail_grup_ordergangguan);

            notic = (TextView) findViewById(R.id.showtotik);
            nama = (TextView) findViewById(R.id.shownama);
            alamat = (TextView) findViewById(R.id.showalamat);
            kontak = (TextView) findViewById(R.id.showkontak);
            jenisgg = (TextView) findViewById(R.id.showjenisgg);
            nonin = (TextView) findViewById(R.id.shownoin);
            status = (TextView) findViewById(R.id.showstatus);
            fotoBuktiImage = (ImageView) findViewById(R.id.detail_foto_bukti);


            // Pasang foto bukti
            Picasso.get().load(getIntent().getStringExtra("bukti")).into(fotoBuktiImage);

            notic.setText(getIntent().getStringExtra("no_tiket"));
            nama.setText(getIntent().getStringExtra("nama"));
            alamat.setText(getIntent().getStringExtra("alamat"));
            kontak.setText(getIntent().getStringExtra("kontak"));
            jenisgg.setText(getIntent().getStringExtra("jenis_gangguan"));
            nonin.setText(getIntent().getStringExtra("no_internet"));
            status.setText(getIntent().getStringExtra("status"));

            stus = status.getText().toString();
            mAuth = FirebaseAuth.getInstance();
            final String currentUID = mAuth.getCurrentUser().getUid(); // Mengambil id telogin
            final String[] namagrup = new String[1];
            mCurrentUserRef = FirebaseFirestore.getInstance();
            mCurrentUserRef.collection("user").document(currentUID)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot,
                                            @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.w("Listen failed.", "Listen failed.", e);
                                return;
                            }

                            if (snapshot != null && snapshot.exists()) {
                                String jenis = snapshot.getString("jenis");

                                if (jenis != null && (jenis.equals("teknisi_pasang_baru") || jenis.equals("teknisi_gangguan"))) {

                                    if (stus.equals("belum")) {
                                        Button but = (Button) findViewById(R.id.kerjakan);
                                        but.setVisibility(View.VISIBLE);
                                        Button but2 = (Button) findViewById(R.id.selesai);
                                        but2.setVisibility(View.GONE);

                                    } else if (stus.equals("proses")) {
                                        Button but = (Button) findViewById(R.id.selesai);
                                        but.setVisibility(View.VISIBLE);
                                        Button but2 = (Button) findViewById(R.id.kerjakan);
                                        but2.setVisibility(View.GONE);

                                    } else if (stus.equals("selesai")) {
                                        Button but = (Button) findViewById(R.id.selesai);
                                        but.setVisibility(View.GONE);
                                        Button but2 = (Button) findViewById(R.id.kerjakan);
                                        but2.setVisibility(View.GONE);

                                    } else {
                                        Button but = (Button) findViewById(R.id.kerjakan);
                                        but.setVisibility(View.VISIBLE);
                                        Button but2 = (Button) findViewById(R.id.selesai);
                                        but2.setVisibility(View.GONE);
                                    }


                                } else {
                                    Button but = (Button) findViewById(R.id.kerjakan);
                                    but.setVisibility(View.GONE);
                                    Button but2 = (Button) findViewById(R.id.selesai);
                                    but2.setVisibility(View.GONE);
                                    Toast.makeText(DetailGrupOrderActivity.this, "ayam", Toast.LENGTH_LONG).show();

                                }

                            } else {
                                Log.d("Current data: null", "Current data: null");

                            }
                        }
                    });


            Button i = (Button) findViewById(R.id.kerjakan);
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailGrupOrderActivity.this, InputReportTeknisiActivity.class);
                    intent.putExtra("no_tiket", noTic);
                    intent.putExtra("jenis", jenis);
                    startActivity(intent);
                    finish();
                }
            });

            Button btn = (Button) findViewById(R.id.selesai);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailGrupOrderActivity.this, InputFinishReportActivity.class);
                    intent.putExtra("no_tiket", noTic);
                    intent.putExtra("jenis", jenis);
                    startActivity(intent);
                    finish();
                }
            });

            Button btn_call = (Button) findViewById(R.id.btn_call);
            btn_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialContactPhone(kontak.getText().toString());
                }

                private void dialContactPhone(final String phoneNumber) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", String.valueOf(phoneNumber), null)));
                }
            });

            Button btn_wa = (Button) findViewById(R.id.btn_wa);
            btn_wa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String text = "";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + kontak.getText().toString() + "&text=" + text));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        Button btn_cancel = (Button) findViewById(R.id.ayahab);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jenis.equals("Pasang Baru")) {
                    mCurrentUserRef.collection("order").document(SCid).update("status", "gagal")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });
                } else {
                    mCurrentUserRef.collection("order").document(noTic).update("status", "gagal")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });
                }
                Toast.makeText(DetailGrupOrderActivity.this,"Pekerjaan Anda Telah Di Hentikan",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DetailGrupOrderActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
