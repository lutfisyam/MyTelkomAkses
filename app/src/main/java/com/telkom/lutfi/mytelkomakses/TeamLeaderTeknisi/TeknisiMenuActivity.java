package com.telkom.lutfi.mytelkomakses.TeamLeaderTeknisi;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TabHost;

import com.telkom.lutfi.mytelkomakses.R;
import com.telkom.lutfi.mytelkomakses.TeamLeaderGrupTeknisi.ListGrupGangguan;
import com.telkom.lutfi.mytelkomakses.TeamLeaderGrupTeknisi.ListGrupPasang;

public class TeknisiMenuActivity extends TabActivity {

        TabHost tabhost;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_teknisi_menu);

            tabhost = (TabHost)findViewById(android.R.id.tabhost);

            TabHost.TabSpec spec;
            Intent intent;

            intent = new Intent().setClass(this, ListTeknisiPasangBaru.class);
            spec = tabhost.newTabSpec("pasang").setIndicator("Pasang Baru",null).setContent(intent);
            tabhost.addTab(spec);

            intent = new Intent().setClass(this, ListTeknisiGangguan.class);
            spec = tabhost.newTabSpec("gangguan").setIndicator("Gangguan",null).setContent(intent);
            tabhost.addTab(spec);


//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Dialog("kon");
//            }
//        });
        }



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_teknisi_menu);
//        CardView cardView = findViewById(R.id.list_teknisipb);
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TeknisiMenuActivity.this, ListTeknisiPasangBaru.class);
//                startActivity(intent);
//            }
//        });
//        CardView cardView1 = findViewById(R.id.list_teknisigangguan);
//        cardView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TeknisiMenuActivity.this, ListTeknisiGangguan.class);
//                startActivity(intent);
//            }
//        });

    }

