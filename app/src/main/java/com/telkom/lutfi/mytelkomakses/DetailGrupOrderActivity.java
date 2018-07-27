package com.telkom.lutfi.mytelkomakses;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class DetailGrupOrderActivity extends AppCompatActivity {

    private TextView sc, nama, alamat, kontak, ncli, ndem, alpro, status;
    String nkontol, JANCOK;
    FirebaseAuth mAuth;
    FirebaseFirestore mCurrentUserRef;
    private ImageView fotoBuktiImage;

    private String SCid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        SCid = getIntent().getStringExtra("sc");

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
        //nkontol = getIntent().getStringExtra("status");
        nkontol = status.getText().toString();
        mAuth = FirebaseAuth.getInstance();
        final String currentUID = mAuth.getCurrentUser().getUid(); // Mengambil id telogin
        final String[] namagrup = new String[1];
        JANCOK = status.getText().toString();
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

                                Toast.makeText(DetailGrupOrderActivity.this, nkontol, Toast.LENGTH_LONG).show();

                                if (nkontol.equals("belum")) {
                                    Button but = (Button) findViewById(R.id.kerjakan);
                                    but.setVisibility(View.VISIBLE);
                                    Button but2 = (Button) findViewById(R.id.selesai);
                                    but2.setVisibility(View.GONE);

                                } else if (nkontol.equals("proses")) {
                                    Button but = (Button) findViewById(R.id.selesai);
                                    but.setVisibility(View.VISIBLE);
                                    Button but2 = (Button) findViewById(R.id.kerjakan);
                                    but2.setVisibility(View.GONE);

                                } else if (nkontol.equals("selesai")) {
                                    Button but = (Button) findViewById(R.id.selesai);
                                    but.setVisibility(View.GONE);
                                    Button but2 = (Button) findViewById(R.id.kerjakan);
                                    but2.setVisibility(View.GONE);

                                } else {
                                    Toast.makeText(DetailGrupOrderActivity.this, "NYASAR", Toast.LENGTH_LONG).show();
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
                intent.putExtra("sc", SCid);
                startActivity(intent);
                finish();
            }
        });

        Button btn = (Button) findViewById(R.id.selesai);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailGrupOrderActivity.this, InputFinishReportActivity.class);
                intent.putExtra("OTNIEL_KONTOL",SCid);
                startActivity(intent);
                finish();
            }
        });
    }
}
