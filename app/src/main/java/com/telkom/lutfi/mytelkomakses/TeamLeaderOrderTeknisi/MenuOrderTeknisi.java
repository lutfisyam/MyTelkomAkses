package com.telkom.lutfi.mytelkomakses.TeamLeaderOrderTeknisi;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.telkom.lutfi.mytelkomakses.R;
import com.telkom.lutfi.mytelkomakses.TeamLeaderGrupTeknisi.ListGrupGangguan;
import com.telkom.lutfi.mytelkomakses.TeamLeaderGrupTeknisi.ListGrupPasang;

public class MenuOrderTeknisi extends TabActivity {
    TabHost tabhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teknisi_menu);

        tabhost = (TabHost) findViewById(android.R.id.tabhost);

        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, ListOrderPasangBaru.class);
        spec = tabhost.newTabSpec("pasang").setIndicator("Order Pasang Baru", null).setContent(intent);
        tabhost.addTab(spec);

        intent = new Intent().setClass(this, ListOrderGangguan.class);
        spec = tabhost.newTabSpec("gangguan").setIndicator("Order Gangguan", null).setContent(intent);
        tabhost.addTab(spec);
    }
}
