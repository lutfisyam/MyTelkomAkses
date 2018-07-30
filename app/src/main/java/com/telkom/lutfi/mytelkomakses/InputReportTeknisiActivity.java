package com.telkom.lutfi.mytelkomakses;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InputReportTeknisiActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private EditText Dropwire, Dropcore, utp, pcv, Traycable, breket, Sclam, Clamhook, Patchcore, Roster, SOC, Pictail, ps;
    Button simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_report_teknisi);
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
        final Intent intent = getIntent();
        final String sc = intent.getStringExtra("sc");

        Dropwire = (EditText) findViewById(R.id.inputDropwire);
        Dropcore = (EditText) findViewById(R.id.inputDropcore);
        utp = (EditText) findViewById(R.id.inputUTP);
        pcv = (EditText) findViewById(R.id.inputPCV);
        Traycable = (EditText) findViewById(R.id.inputTrayCabel);
        breket = (EditText) findViewById(R.id.inputBreker);
        Sclam = (EditText) findViewById(R.id.inputSclam);
        Clamhook = (EditText) findViewById(R.id.inputClamhook);
        Patchcore = (EditText) findViewById(R.id.inputPatchcore);
        Roster = (EditText) findViewById(R.id.inputRoster);
        SOC = (EditText) findViewById(R.id.inputSOC);
        Pictail = (EditText) findViewById(R.id.inputPictTail);
        ps = (EditText) findViewById(R.id.inputps);
        simpan = (Button) findViewById(R.id.simpan_material);





        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> input = new HashMap<>();
                input.put("Dropwire",Dropwire.getText().toString());
                input.put("Dropcore",Dropcore.getText().toString());
                input.put("utp",utp.getText().toString());
                input.put("pcv",pcv.getText().toString());
                input.put("Traycable",Traycable.getText().toString());
                input.put("breket",breket.getText().toString());
                input.put("Sclam",Sclam.getText().toString());
                input.put("Clamhook",Clamhook.getText().toString());
                input.put("Patchcore",Patchcore.getText().toString());
                input.put("Roster",Roster.getText().toString());
                input.put("SOC",SOC.getText().toString());
                input.put("Pictail",Pictail.getText().toString());
                input.put("ps",ps.getText().toString());
                Date tanggal = Calendar.getInstance().getTime();

                mFireStore.collection("order")
                        .document(sc)
                        .collection("material")
                        .document("1")
                        .set(input)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {     }
                        });
                mFireStore.collection("order").document(sc).update("status", "proses")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {     }
                        });
                mFireStore.collection("order").document(sc).update("waktumulai", tanggal)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {     }
                        });

                Intent intent = new Intent(InputReportTeknisiActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });




    }
}
