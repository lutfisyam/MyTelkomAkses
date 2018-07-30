package com.telkom.lutfi.mytelkomakses.TeamLeaderGrupTeknisi;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

import com.telkom.lutfi.mytelkomakses.R;
import com.telkom.lutfi.mytelkomakses.TeamLeaderTeknisi.ListTeknisiGangguan;
import com.telkom.lutfi.mytelkomakses.TeamLeaderTeknisi.ListTeknisiPasangBaru;

public class MenuGrupTeknisi extends TabActivity {
    TabHost tabhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teknisi_menu);

        tabhost = (TabHost) findViewById(android.R.id.tabhost);

        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, ListGrupPasang.class);
        spec = tabhost.newTabSpec("pasang").setIndicator("Grup Pasang Baru", null).setContent(intent);
        tabhost.addTab(spec);

        intent = new Intent().setClass(this, ListGrupGangguan.class);
        spec = tabhost.newTabSpec("gangguan").setIndicator("Grup Gangguan", null).setContent(intent);
        tabhost.addTab(spec);

        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
            TextView textView = tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            textView.setTextColor(getResources().getColor(R.color.colorWhite));
        }

    }
}
