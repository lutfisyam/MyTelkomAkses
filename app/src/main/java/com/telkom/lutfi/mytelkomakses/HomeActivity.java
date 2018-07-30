package com.telkom.lutfi.mytelkomakses;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    // deklasajfvad
    FirebaseAuth mAuth;
    FirebaseFirestore mCurrentUserRef;

    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cobaExport();

        mAuth = FirebaseAuth.getInstance();
        final String currentUID = mAuth.getCurrentUser().getUid(); // Mengambil id telogin
        final String[] namagrup = new String[1];

        // fungsi bawaan firestore (mengambil data scr realtime)
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
//                            jenisText.setText(jenis);

                            namagrup[0] = snapshot.getString("nama_grup");

                            if (jenis != null && (jenis.equals("teknisi_pasang_baru") || jenis.equals("teknisi_gangguan"))) {
                                CardView cardView = (CardView) findViewById(R.id.gotoTeamLeader);
                                //CardView cardView = (CardView) findViewById(R.id.gotoTeknisi);
                                cardView.setVisibility(View.GONE);
                            } else {
                                CardView cardView = (CardView) findViewById(R.id.gotoTeknisi);
                                //CardView cardView = (CardView) findViewById(R.id.gotoTeamLeader);
                                cardView.setVisibility(View.GONE);
                            }

                        } else {
                            Log.d("Current data: null", "Current data: null");
                        }
                    }
                });

        CardView i = (CardView) findViewById(R.id.gotoTeamLeader);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TeamLeaderActivity.class);
                startActivity(intent);
            }
        });

        CardView card = (CardView) findViewById(R.id.gotoTeknisi);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TeknisiActivity.class);
                intent.putExtra("montu", currentUID);
                intent.putExtra("nama_grup", namagrup[0]);
                startActivity(intent);
            }
        });
    }

    private void cobaExport() {
        Button button = findViewById(R.id.cobaexport);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                String fileName = "AnalysisData.xlsx";
                String filePath = baseDir + File.separator + fileName;
                File file = new File(filePath);
                CSVWriter writer = null;
                // File exist
//                if (file.exists() && !file.isDirectory()) {
//                    try {
//                        FileWriter fileWriter = new FileWriter(filePath, true);
//                        writer = new CSVWriter(fileWriter);
//                        String[] data = {"Halo1", "Halo2", "Halo3", "Halo4"};
//                        writer.writeNext(data);
//                        writer.close();
//                        Log.d("COBA EKSPOR", "BASE DIR : " + baseDir);
//                        Log.d("COBA EKSPOR", "fileName : " + fileName);
//                        Log.d("COBA EKSPOR", "filePath : " + filePath);
//                        Log.d("COBA EKSPOR", "file : " + file);
//                        Log.d("COBA EKSPOR", "fileWriter : " + fileWriter);
//                        Log.d("COBA EKSPOR", "writer : " + writer);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

//                } else {
                    try {
                        writer = new CSVWriter(new FileWriter(filePath));
                        String[] data = {"Halo1", "Halo2", "Halo3", "Halo4"};
                        writer.writeNext(data);
                        writer.close();
                        Log.d("COBA EKSPOR", "BASE DIR : " + baseDir);
                        Log.d("COBA EKSPOR", "fileName : " + fileName);
                        Log.d("COBA EKSPOR", "filePath : " + filePath);
                        Log.d("COBA EKSPOR", "file : " + file);
                        Log.d("COBA EKSPOR", "writer : " + writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                }
            }
        });
    }
}
