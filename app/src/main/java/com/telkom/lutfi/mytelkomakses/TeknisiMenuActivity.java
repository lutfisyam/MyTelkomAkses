package com.telkom.lutfi.mytelkomakses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class TeknisiMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teknisi_menu);

        CardView cardView = findViewById(R.id.list_teknisipb);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeknisiMenuActivity.this, ListTeknisiPasangBaru.class);
                startActivity(intent);
            }
        });
        CardView cardView1 = findViewById(R.id.list_teknisigangguan);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeknisiMenuActivity.this, ListTeknisiGangguan.class);
                startActivity(intent);
            }
        });

    }
}
